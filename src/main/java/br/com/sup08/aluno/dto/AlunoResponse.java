package br.com.sup08.aluno.dto;

import br.com.sup08.aluno.domain.Aluno;
import java.time.Instant;
import java.time.LocalDate;

public record AlunoResponse(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        Instant createdAt,
        Instant updatedAt
) {
    public static AlunoResponse fromEntity(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getDataNascimento(),
                aluno.getCreatedAt(),
                aluno.getUpdatedAt()
        );
    }
}