package br.com.sup08.aula.repository;

import br.com.sup08.aula.domain.Aula;
import br.com.sup08.aula.domain.StatusAula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    // RG0602: Sobreposição na mesma turma
    @Query("SELECT COUNT(a) > 0 FROM Aula a WHERE a.turma.id = :turmaId " +
            "AND a.status <> 'CANCELADA' AND a.inicio < :fim AND a.fim > :inicio " +
            "AND (:aulaId IS NULL OR a.id <> :aulaId)")
    boolean existeSobreposicaoTurma(@Param("turmaId") Long turmaId,
                                    @Param("inicio") LocalDateTime inicio,
                                    @Param("fim") LocalDateTime fim,
                                    @Param("aulaId") Long aulaId);

    // RG0603: Sobreposição para o mesmo professor
    @Query("SELECT COUNT(a) > 0 FROM Aula a WHERE a.turma.professor.id = :professorId " +
            "AND a.status <> 'CANCELADA' AND a.inicio < :fim AND a.fim > :inicio " +
            "AND (:aulaId IS NULL OR a.id <> :aulaId)")
    boolean existeSobreposicaoProfessor(@Param("professorId") Long professorId,
                                        @Param("inicio") LocalDateTime inicio,
                                        @Param("fim") LocalDateTime fim,
                                        @Param("aulaId") Long aulaId);

    // RG0606: Aula em andamento por turma
    boolean existsByTurmaIdAndStatus(Long turmaId, StatusAula status);

    // RG0405: Buscar aulas pendentes de uma turma para cancelamento em cascata
    List<Aula> findByTurmaIdAndStatus(Long turmaId, StatusAula status);

    // RG0609: Buscar aulas finalizadas para somar carga horária
    List<Aula> findByTurmaIdAndStatusOrderByInicioAsc(Long turmaId, StatusAula status);

    // RF0605: Filtros de listagem
    @Query("SELECT a FROM Aula a WHERE " +
            "(:turmaId IS NULL OR a.turma.id = :turmaId) AND " +
            "(:professorId IS NULL OR a.turma.professor.id = :professorId) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:inicio IS NULL OR a.inicio >= :inicio) AND " +
            "(:fim IS NULL OR a.fim <= :fim)")
    Page<Aula> buscarComFiltros(@Param("turmaId") Long turmaId,
                                @Param("professorId") Long professorId,
                                @Param("status") StatusAula status,
                                @Param("inicio") LocalDateTime inicio,
                                @Param("fim") LocalDateTime fim,
                                Pageable pageable);

    // RF0607: Agenda do professor
    @Query("SELECT a FROM Aula a WHERE a.turma.professor.id = :professorId " +
            "AND a.inicio >= :inicio AND a.fim <= :fim AND a.status <> 'CANCELADA' " +
            "ORDER BY a.inicio ASC")
    List<Aula> buscarAgendaProfessor(@Param("professorId") Long professorId,
                                     @Param("inicio") LocalDateTime inicio,
                                     @Param("fim") LocalDateTime fim);
}