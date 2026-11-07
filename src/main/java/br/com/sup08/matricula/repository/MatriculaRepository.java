package br.com.sup08.matricula.repository;

import br.com.sup08.matricula.domain.Matricula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByAlunoIdAndTurmaId(Long alunoId, Long turmaId);

    Optional<Matricula> findByAlunoIdAndTurmaId(Long alunoId, Long turmaId);

    Page<Matricula> findByTurmaId(Long turmaId, Pageable pageable);

    List<Matricula> findByAlunoId(Long alunoId);
}