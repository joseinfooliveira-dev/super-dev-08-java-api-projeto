package br.com.sup08.matricula.service;

import br.com.sup08.aluno.domain.Aluno;
import br.com.sup08.aluno.dto.AlunoResponse;
import br.com.sup08.aluno.repository.AlunoRepository;
import br.com.sup08.aula.domain.StatusAula; // NOVO IMPORT
import br.com.sup08.aula.repository.AulaRepository; // NOVO IMPORT
import br.com.sup08.common.exception.RecursoNaoEncontradoException;
import br.com.sup08.common.exception.RegraNegocioException;
import br.com.sup08.matricula.domain.Matricula;
import br.com.sup08.matricula.dto.MatriculaLoteRequest;
import br.com.sup08.matricula.dto.MatriculaRequest;
import br.com.sup08.matricula.dto.MatriculaResponse;
import br.com.sup08.matricula.repository.MatriculaRepository;
import br.com.sup08.turma.domain.StatusTurma;
import br.com.sup08.turma.domain.Turma;
import br.com.sup08.turma.dto.TurmaResponse;
import br.com.sup08.turma.repository.TurmaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository repository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final AulaRepository aulaRepository; // NOVO

    public MatriculaService(MatriculaRepository repository,
                            AlunoRepository alunoRepository,
                            TurmaRepository turmaRepository,
                            AulaRepository aulaRepository) { // ATUALIZADO
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.aulaRepository = aulaRepository;
    }

    @Transactional
    public MatriculaResponse matricular(MatriculaRequest request) {
        Turma turma = validarTurma(request.turmaId());
        Aluno aluno = validarAluno(request.alunoId());

        if (repository.existsByAlunoIdAndTurmaId(request.alunoId(), request.turmaId())) {
            throw new RegraNegocioException("RG0500", "Aluno já matriculado nesta turma");
        }

        Matricula matricula = new Matricula(aluno, turma);
        return MatriculaResponse.fromEntity(repository.save(matricula));
    }

    @Transactional
    public List<MatriculaResponse> matricularLote(MatriculaLoteRequest request) {
        Turma turma = validarTurma(request.turmaId());
        List<Matricula> matriculasParaSalvar = new ArrayList<>();

        for (Long alunoId : request.alunosIds()) {
            Aluno aluno = validarAluno(alunoId);

            if (repository.existsByAlunoIdAndTurmaId(alunoId, request.turmaId())) {
                throw new RegraNegocioException("RG0500", "Aluno com id " + alunoId + " já matriculado nesta turma");
            }
            matriculasParaSalvar.add(new Matricula(aluno, turma));
        }

        List<Matricula> salvas = repository.saveAll(matriculasParaSalvar);
        return salvas.stream().map(MatriculaResponse::fromEntity).toList();
    }

    @Transactional
    public void remover(Long alunoId, Long turmaId) {
        Matricula matricula = repository.findByAlunoIdAndTurmaId(alunoId, turmaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada"));

        // RG0503: Verificar se a turma possui aula FINALIZADA antes de remover.
        boolean temAulaFinalizada = aulaRepository.existsByTurmaIdAndStatus(turmaId, StatusAula.FINALIZADA);
        if (temAulaFinalizada) {
            throw new RegraNegocioException("RG0503", "Matrícula não pode ser removida pois a turma já possui aulas finalizadas");
        }

        repository.delete(matricula);
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponse> listarAlunosDaTurma(Long turmaId, Pageable pageable) {
        if (!turmaRepository.existsById(turmaId)) {
            throw new RecursoNaoEncontradoException("Turma não encontrada");
        }
        return repository.findByTurmaId(turmaId, pageable)
                .map(m -> AlunoResponse.fromEntity(m.getAluno()));
    }

    @Transactional(readOnly = true)
    public List<TurmaResponse> listarTurmasDoAluno(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado");
        }
        return repository.findByAlunoId(alunoId).stream()
                .map(m -> TurmaResponse.fromEntity(m.getTurma()))
                .toList();
    }

    private Turma validarTurma(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada"));
        if (turma.getStatus() != StatusTurma.ATIVA) {
            throw new RegraNegocioException("RG0501", "Só é permitido matricular em turma ATIVA");
        }
        return turma;
    }

    private Aluno validarAluno(Long alunoId) {
        return alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));
    }
}