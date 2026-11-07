package br.com.sup08.aula.dto;

import br.com.sup08.aula.domain.Aula;
import br.com.sup08.aula.domain.StatusAula;
import java.time.Instant;
import java.time.LocalDateTime;

public record AulaResponse(
        Long id,
        LocalDateTime inicio,
        LocalDateTime fim,
        StatusAula status,
        String motivoCancelamento,
        Long turmaId,
        String turmaNome,
        Long professorId,
        String professorNome,
        Instant createdAt,
        Instant updatedAt
) {
    public static AulaResponse fromEntity(Aula aula) {
        return new AulaResponse(
                aula.getId(),
                aula.getInicio(),
                aula.getFim(),
                aula.getStatus(),
                aula.getMotivoCancelamento(),
                aula.getTurma().getId(),
                aula.getTurma().getNome(),
                aula.getTurma().getProfessor().getId(),
                aula.getTurma().getProfessor().getNome(),
                aula.getCreatedAt(),
                aula.getUpdatedAt()
        );
    }
}