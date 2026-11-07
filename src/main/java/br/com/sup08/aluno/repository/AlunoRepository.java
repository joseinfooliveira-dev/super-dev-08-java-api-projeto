package br.com.sup08.aluno.repository;

import br.com.sup08.aluno.domain.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByCpf(String cpf);

    @Query("SELECT a FROM Aluno a WHERE " +
            "(:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:cpf IS NULL OR a.cpf = :cpf)")
    Page<Aluno> buscarComFiltros(@Param("nome") String nome,
                                 @Param("cpf") String cpf,
                                 Pageable pageable);
}