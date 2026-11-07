package br.com.sup08.professor.repository;

import br.com.sup08.professor.domain.Professor;
import br.com.sup08.professor.domain.StatusProfessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query("SELECT p FROM Professor p WHERE " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:status IS NULL OR p.status = :status)")
    Page<Professor> buscarComFiltros(@Param("nome") String nome,
                                     @Param("status") StatusProfessor status,
                                     Pageable pageable);
}