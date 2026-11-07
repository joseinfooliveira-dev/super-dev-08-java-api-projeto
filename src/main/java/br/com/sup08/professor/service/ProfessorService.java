package br.com.sup08.professor.service;

import br.com.sup08.common.exception.RecursoNaoEncontradoException;
import br.com.sup08.common.exception.RegraNegocioException; // NOVO IMPORT
import br.com.sup08.professor.domain.Professor;
import br.com.sup08.professor.domain.StatusProfessor;
import br.com.sup08.professor.dto.ProfessorCreateRequest;
import br.com.sup08.professor.dto.ProfessorResponse;
import br.com.sup08.professor.dto.ProfessorUpdateRequest;
import br.com.sup08.professor.repository.ProfessorRepository;
import br.com.sup08.turma.domain.StatusTurma; // NOVO IMPORT
import br.com.sup08.turma.repository.TurmaRepository; // NOVO IMPORT
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;
    private final TurmaRepository turmaRepository; // NOVO

    public ProfessorService(ProfessorRepository repository, TurmaRepository turmaRepository) { // ATUALIZADO
        this.repository = repository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public ProfessorResponse cadastrar(ProfessorCreateRequest request) {
        Professor professor = new Professor(request.nome());
        professor.setStatus(StatusProfessor.ATIVO);
        professor = repository.save(professor);
        return ProfessorResponse.fromEntity(professor);
    }

    @Transactional
    public ProfessorResponse editar(Long id, ProfessorUpdateRequest request) {
        Professor professor = buscarPorId(id);
        professor.setNome(request.nome());
        professor = repository.save(professor);
        return ProfessorResponse.fromEntity(professor);
    }

    @Transactional
    public ProfessorResponse inativar(Long id) {
        Professor professor = buscarPorId(id);

        // RG0200: Valida se há turma ATIVA atrelada
        boolean temTurmaAtiva = turmaRepository.existsByProfessorIdAndStatus(id, StatusTurma.ATIVA);
        if (temTurmaAtiva) {
            throw new RegraNegocioException("RG0200", "Professor não pode ser inativado pois possui turma ativa vinculada");
        }

        if (professor.getStatus() == StatusProfessor.INATIVO) {
            return ProfessorResponse.fromEntity(professor);
        }

        professor.setStatus(StatusProfessor.INATIVO);
        professor = repository.save(professor);
        return ProfessorResponse.fromEntity(professor);
    }

    @Transactional
    public ProfessorResponse reativar(Long id) {
        Professor professor = buscarPorId(id);

        if (professor.getStatus() == StatusProfessor.ATIVO) {
            return ProfessorResponse.fromEntity(professor);
        }

        professor.setStatus(StatusProfessor.ATIVO);
        professor = repository.save(professor);
        return ProfessorResponse.fromEntity(professor);
    }

    @Transactional(readOnly = true)
    public Page<ProfessorResponse> listar(String nome, StatusProfessor status, Pageable pageable) {
        return repository.buscarComFiltros(nome, status, pageable)
                .map(ProfessorResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProfessorResponse buscarPorIdResponse(Long id) {
        return ProfessorResponse.fromEntity(buscarPorId(id));
    }

    private Professor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado com o id: " + id));
    }
}