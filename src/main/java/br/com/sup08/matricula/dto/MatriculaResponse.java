package br.com.sup08.matricula.dto;

import br.com.sup08.matricula.domain.Matricula;
import java.time.Instant;

public record MatriculaResponse(
        Long id,
        Long alunoId,
        String alunoNome,
        Long turmaId,
        String turmaNome,
        Instant createdAt
) {
    public static MatriculaResponse fromEntity(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getId(),
                matricula.getAluno().getId(),
                matricula.getAluno().getNome(),
                matricula.getTurma().getId(),
                matricula.getTurma().getNome(),
                matricula.getCreatedAt()
        );
    }
}