package br.com.sup08.turma.repository;

import br.com.sup08.turma.domain.StatusTurma;
import br.com.sup08.turma.domain.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    boolean existsByNomeIgnoreCaseAndUnidadeId(String nome, Long unidadeId);

    // --- NOVO MÉTODO ADICIONADO AQUI ---
    boolean existsByProfessorIdAndStatus(Long professorId, StatusTurma status);

    @Query("SELECT t FROM Turma t WHERE " +
            "(:nome IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:professorId IS NULL OR t.professor.id = :professorId) AND " +
            "(:unidadeId IS NULL OR t.unidade.id = :unidadeId) AND " +
            "(:status IS NULL OR t.status = :status)")
    Page<Turma> buscarComFiltros(@Param("nome") String nome,
                                 @Param("professorId") Long professorId,
                                 @Param("unidadeId") Long unidadeId,
                                 @Param("status") StatusTurma status,
                                 Pageable pageable);
}