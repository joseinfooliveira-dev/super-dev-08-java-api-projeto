package br.com.sup08.turma.service;

import br.com.sup08.aula.domain.Aula;
import br.com.sup08.aula.domain.StatusAula;
import br.com.sup08.aula.repository.AulaRepository;
import br.com.sup08.common.exception.RecursoNaoEncontradoException;
import br.com.sup08.common.exception.RegraNegocioException;
import br.com.sup08.matricula.repository.MatriculaRepository;
import br.com.sup08.professor.domain.Professor;
import br.com.sup08.professor.domain.StatusProfessor;
import br.com.sup08.professor.repository.ProfessorRepository;
import br.com.sup08.turma.domain.StatusTurma;
import br.com.sup08.turma.domain.Turma;
import br.com.sup08.turma.dto.*;
import br.com.sup08.turma.repository.TurmaRepository;
import br.com.sup08.unidade.domain.Unidade;
import br.com.sup08.unidade.repository.UnidadeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository repository;
    private final ProfessorRepository professorRepository;
    private final UnidadeRepository unidadeRepository;
    private final AulaRepository aulaRepository; // NOVO
    private final MatriculaRepository matriculaRepository; // NOVO

    public TurmaService(TurmaRepository repository,
                        ProfessorRepository professorRepository,
                        UnidadeRepository unidadeRepository,
                        AulaRepository aulaRepository,
                        MatriculaRepository matriculaRepository) { // ATUALIZADO
        this.repository = repository;
        this.professorRepository = professorRepository;
        this.unidadeRepository = unidadeRepository;
        this.aulaRepository = aulaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @Transactional
    public TurmaResponse cadastrar(TurmaCreateRequest request) {
        Professor professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));

        if (professor.getStatus() != StatusProfessor.ATIVO) {
            throw new RegraNegocioException("RG0400", "Professor deve estar ativo");
        }

        Unidade unidade = unidadeRepository.findById(request.unidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada"));

        if (repository.existsByNomeIgnoreCaseAndUnidadeId(request.nome(), request.unidadeId())) {
            throw new RegraNegocioException("RG0402", "Já existe uma turma com este nome nesta unidade");
        }

        Turma turma = new Turma(request.nome(), request.cargaHoraria(), professor, unidade);
        turma = repository.save(turma);
        return TurmaResponse.fromEntity(turma);
    }

    @Transactional
    public TurmaResponse editar(Long id, TurmaUpdateRequest request) {
        Turma turma = buscarPorId(id);

        if (turma.getStatus() == StatusTurma.CONCLUIDA) {
            throw new RegraNegocioException("RG0404", "Turma concluída não pode ser editada");
        }

        if (!turma.getNome().equalsIgnoreCase(request.nome()) &&
                repository.existsByNomeIgnoreCaseAndUnidadeId(request.nome(), turma.getUnidade().getId())) {
            throw new RegraNegocioException("RG0402", "Já existe uma turma com este nome nesta unidade");
        }

        if (!turma.getProfessor().getId().equals(request.professorId())) {
            Professor novoProfessor = professorRepository.findById(request.professorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));
            if (novoProfessor.getStatus() != StatusProfessor.ATIVO) {
                throw new RegraNegocioException("RG0404", "Novo professor deve estar ativo");
            }
            turma.setProfessor(novoProfessor);
        }

        // RG0408: Validar se a nova carga horária não é inferior às horas já realizadas
        int horasRealizadas = calcularHorasRealizadas(turma.getId());
        if (request.cargaHoraria() < horasRealizadas) {
            throw new RegraNegocioException("RG0408", "Carga horária não pode ser menor que as horas já realizadas (" + horasRealizadas + "h)");
        }

        turma.setNome(request.nome());
        turma.setCargaHoraria(request.cargaHoraria());
        turma = repository.save(turma);
        return TurmaResponse.fromEntity(turma);
    }

    @Transactional
    public TurmaResponse inativar(Long id) {
        Turma turma = buscarPorId(id);

        if (turma.getStatus() == StatusTurma.INATIVA) {
            return TurmaResponse.fromEntity(turma);
        }

        // RG0405: Verificar se há aula EM_ANDAMENTO
        boolean temAulaEmAndamento = aulaRepository.existsByTurmaIdAndStatus(id, StatusAula.EM_ANDAMENTO);
        if (temAulaEmAndamento) {
            throw new RegraNegocioException("RG0405", "Turma não pode ser inativada pois possui aula em andamento");
        }

        // RG0405: Cancelar todas as aulas PENDENTES em cascata
        List<Aula> aulasPendentes = aulaRepository.findByTurmaIdAndStatus(id, StatusAula.PENDENTE);
        for (Aula aula : aulasPendentes) {
            aula.setStatus(StatusAula.CANCELADA);
            aula.setMotivoCancelamento("Cancelada em cascata pela inativação da turma");
        }
        aulaRepository.saveAll(aulasPendentes);

        turma.setStatus(StatusTurma.INATIVA);
        turma = repository.save(turma);
        return TurmaResponse.fromEntity(turma);
    }

    @Transactional(readOnly = true)
    public Page<TurmaResponse> listar(String nome, Long professorId, Long unidadeId, StatusTurma status, Pageable pageable) {
        return repository.buscarComFiltros(nome, professorId, unidadeId, status, pageable)
                .map(TurmaResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public TurmaResponse buscarPorIdResponse(Long id) {
        return TurmaResponse.fromEntity(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public TurmaProgressoResponse obterProgresso(Long id) {
        Turma turma = buscarPorId(id);

        int horasRealizadas = calcularHorasRealizadas(id);
        long totalAlunos = matriculaRepository.findByTurmaId(id, Pageable.unpaged()).getTotalElements();

        int horasRestantes = turma.getCargaHoraria() - horasRealizadas;

        return new TurmaProgressoResponse(
                turma.getCargaHoraria(),
                horasRealizadas,
                horasRestantes,
                totalAlunos
        );
    }

    // Método auxiliar para calcular horas realizadas (RF0405 / RG0408)
    private int calcularHorasRealizadas(Long turmaId) {
        List<Aula> finalizadas = aulaRepository.findByTurmaIdAndStatusOrderByInicioAsc(turmaId, StatusAula.FINALIZADA);
        long minutosRealizados = finalizadas.stream()
                .mapToLong(a -> Duration.between(a.getInicio(), a.getFim()).toMinutes())
                .sum();
        return (int) (minutosRealizados / 60);
    }

    private Turma buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada com o id: " + id));
    }
}