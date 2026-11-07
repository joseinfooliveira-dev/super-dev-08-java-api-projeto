package br.com.sup08.professor.dto;

import br.com.sup08.professor.domain.Professor;
import br.com.sup08.professor.domain.StatusProfessor;
import java.time.Instant;

public record ProfessorResponse(
        Long id,
        String nome,
        StatusProfessor status,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProfessorResponse fromEntity(Professor professor) {
        return new ProfessorResponse(
                professor.getId(),
                professor.getNome(),
                professor.getStatus(),
                professor.getCreatedAt(),
                professor.getUpdatedAt()
        );
    }
}