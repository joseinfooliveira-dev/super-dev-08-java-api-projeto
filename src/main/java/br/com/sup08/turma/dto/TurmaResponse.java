package br.com.sup08.turma.dto;

import br.com.sup08.turma.domain.StatusTurma;
import br.com.sup08.turma.domain.Turma;
import java.time.Instant;

public record TurmaResponse(
        Long id,
        String nome,
        Integer cargaHoraria,
        StatusTurma status,
        Long professorId,
        String professorNome,
        Long unidadeId,
        String unidadeNome,
        Instant createdAt,
        Instant updatedAt
) {
    public static TurmaResponse fromEntity(Turma turma) {
        return new TurmaResponse(
                turma.getId(),
                turma.getNome(),
                turma.getCargaHoraria(),
                turma.getStatus(),
                turma.getProfessor().getId(),
                turma.getProfessor().getNome(),
                turma.getUnidade().getId(),
                turma.getUnidade().getNome(),
                turma.getCreatedAt(),
                turma.getUpdatedAt()
        );
    }
}