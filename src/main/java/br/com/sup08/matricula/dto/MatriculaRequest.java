package br.com.sup08.matricula.dto;

import jakarta.validation.constraints.NotNull;

public record MatriculaRequest(
        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId,

        @NotNull(message = "O ID da turma é obrigatório")
        Long turmaId
) {}