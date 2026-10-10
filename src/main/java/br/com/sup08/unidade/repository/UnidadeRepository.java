package br.com.sup08.unidade.repository;

import br.com.sup08.unidade.domain.Unidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    boolean existsBySiglaIgnoreCase(String sigla);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    @Query("""
        SELECT u FROM Unidade u
        WHERE (:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:sigla IS NULL OR LOWER(u.sigla) LIKE LOWER(CONCAT('%', :sigla, '%')))
    """)
    Page<Unidade> pesquisar(@Param("nome") String nome, @Param("sigla") String sigla, Pageable pageable);
}