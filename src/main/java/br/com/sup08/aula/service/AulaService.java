package br.com.sup08.aula.service;

import br.com.sup08.aula.domain.Aula;
import br.com.sup08.aula.domain.StatusAula;
import br.com.sup08.aula.dto.*;
import br.com.sup08.aula.repository.AulaRepository;
import br.com.sup08.common.exception.RecursoNaoEncontradoException;
import br.com.sup08.common.exception.RegraNegocioException;
import br.com.sup08.turma.domain.StatusTurma;
import br.com.sup08.turma.domain.Turma;
import br.com.sup08.turma.repository.TurmaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AulaService {

    private final AulaRepository repository;
    private final TurmaRepository turmaRepository;

    public AulaService(AulaRepository repository, TurmaRepository turmaRepository) {
        this.repository = repository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public AulaResponse agendar(AulaCreateRequest request) {
        Turma turma = turmaRepository.findById(request.turmaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada"));

        if (turma.getStatus() != StatusTurma.ATIVA) {
            throw new RegraNegocioException("RG0601", "Só é possível agendar aulas em turmas ATIVAS");
        }

        validarDatas(request.inicio(), request.fim(), null);

        // Valida sobreposição na turma (RG0602) e no professor (RG0603)
        if (repository.existeSobreposicaoTurma(turma.getId(), request.inicio(), request.fim(), null)) {
            throw new RegraNegocioException("RG0602", "Já existe uma aula para esta turma neste horário");
        }
        if (repository.existeSobreposicaoProfessor(turma.getProfessor().getId(), request.inicio(), request.fim(), null)) {
            throw new RegraNegocioException("RG0603", "O professor já possui aula neste horário em outra turma");
        }

        Aula aula = new Aula(request.inicio(), request.fim(), turma);
        aula = repository.save(aula);
        return AulaResponse.fromEntity(aula);
    }

    @Transactional
    public AulaResponse editar(Long id, AulaUpdateRequest request) {
        Aula aula = buscarPorId(id);

        if (aula.getStatus() != StatusAula.PENDENTE) {
            throw new RegraNegocioException("RG0607", "Só é possível editar aulas PENDENTES");
        }

        validarDatas(request.inicio(), request.fim(), aula.getId());

        if (repository.existeSobreposicaoTurma(aula.getTurma().getId(), request.inicio(), request.fim(), aula.getId())) {
            throw new RegraNegocioException("RG0602", "Já existe uma aula para esta turma neste horário");
        }
        if (repository.existeSobreposicaoProfessor(aula.getTurma().getProfessor().getId(), request.inicio(), request.fim(), aula.getId())) {
            throw new RegraNegocioException("RG0603", "O professor já possui aula neste horário");
        }

        aula.setInicio(request.inicio());
        aula.setFim(request.fim());
        aula = repository.save(aula);
        return AulaResponse.fromEntity(aula);
    }

    @Transactional
    public AulaResponse iniciar(Long id) {
        Aula aula = buscarPorId(id);

        if (aula.getStatus() != StatusAula.PENDENTE) {
            throw new RegraNegocioException("RG0605", "Apenas aulas PENDENTES podem ser iniciadas");
        }

        if (repository.existsByTurmaIdAndStatus(aula.getTurma().getId(), StatusAula.EM_ANDAMENTO)) {
            throw new RegraNegocioException("RG0606", "Já existe uma aula em andamento para esta turma");
        }

        aula.setStatus(StatusAula.EM_ANDAMENTO);
        aula = repository.save(aula);
        return AulaResponse.fromEntity(aula);
    }

    @Transactional
    public AulaResponse finalizar(Long id) {
        Aula aula = buscarPorId(id);

        if (aula.getStatus() != StatusAula.EM_ANDAMENTO) {
            throw new RegraNegocioException("RG0605", "Apenas aulas EM_ANDAMENTO podem ser finalizadas");
        }

        // RG0609: Verificar se a soma das horas finalizadas + a atual não excede a carga horária
        Turma turma = aula.getTurma();
        List<Aula> finalizadas = repository.findByTurmaIdAndStatusOrderByInicioAsc(turma.getId(), StatusAula.FINALIZADA);

        long minutosRealizados = finalizadas.stream()
                .mapToLong(a -> Duration.between(a.getInicio(), a.getFim()).toMinutes())
                .sum();

        long minutosAulaAtual = Duration.between(aula.getInicio(), aula.getFim()).toMinutes();
        long cargaHorariaMinutos = turma.getCargaHoraria() * 60L;

        if ((minutosRealizados + minutosAulaAtual) > cargaHorariaMinutos) {
            throw new RegraNegocioException("RG0609", "A finalização desta aula excede a carga horária da turma");
        }

        aula.setStatus(StatusAula.FINALIZADA);
        aula = repository.save(aula);

        // RG0407: Verifica se a turma deve ser concluída
        verificarConclusaoTurma(turma);

        return AulaResponse.fromEntity(aula);
    }

    @Transactional
    public AulaResponse cancelar(Long id, AulaCancelamentoRequest request) {
        Aula aula = buscarPorId(id);

        if (aula.getStatus() == StatusAula.FINALIZADA || aula.getStatus() == StatusAula.CANCELADA) {
            throw new RegraNegocioException("RG0605", "Aulas FINALIZADAS ou CANCELADAS não podem ser canceladas novamente");
        }

        aula.setStatus(StatusAula.CANCELADA);
        aula.setMotivoCancelamento(request.motivo());
        aula = repository.save(aula);
        return AulaResponse.fromEntity(aula);
    }

    @Transactional(readOnly = true)
    public Page<AulaResponse> listar(Long turmaId, Long professorId, StatusAula status, LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        return repository.buscarComFiltros(turmaId, professorId, status, inicio, fim, pageable)
                .map(AulaResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AulaResponse buscarPorIdResponse(Long id) {
        return AulaResponse.fromEntity(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<AulaResponse> obterAgendaProfessor(Long professorId, LocalDateTime inicio, LocalDateTime fim) {
        return repository.buscarAgendaProfessor(professorId, inicio, fim)
                .stream().map(AulaResponse::fromEntity).toList();
    }

    // Valida RG0600 (fim > inicio), RG0608 (não pode ser no passado) e RG0611 (max 24h)
    private void validarDatas(LocalDateTime inicio, LocalDateTime fim, Long aulaId) {
        if (!fim.isAfter(inicio)) {
            throw new RegraNegocioException("RG0600", "A data/hora de fim deve ser maior que a de início");
        }
        if (aulaId == null && inicio.isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("RG0608", "Não é possível agendar aula com data/hora no passado");
        }
        if (Duration.between(inicio, fim).toHours() > 24) {
            throw new RegraNegocioException("RG0611", "A duração da aula não pode exceder 24 horas");
        }
    }

    private void verificarConclusaoTurma(Turma turma) {
        List<Aula> finalizadas = repository.findByTurmaIdAndStatusOrderByInicioAsc(turma.getId(), StatusAula.FINALIZADA);
        long minutosRealizados = finalizadas.stream()
                .mapToLong(a -> Duration.between(a.getInicio(), a.getFim()).toMinutes())
                .sum();

        long cargaHorariaMinutos = turma.getCargaHoraria() * 60L;

        if (minutosRealizados >= cargaHorariaMinutos && turma.getStatus() == StatusTurma.ATIVA) {
            turma.setStatus(StatusTurma.CONCLUIDA);
            turmaRepository.save(turma);
        }
    }

    private Aula buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aula não encontrada com o id: " + id));
    }
}