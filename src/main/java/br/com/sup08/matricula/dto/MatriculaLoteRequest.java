package br.com.sup08.matricula.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MatriculaLoteRequest(
        @NotNull(message = "O ID da turma é obrigatório")
        Long turmaId,

        @NotEmpty(message = "A lista de alunos não pode ser vazia")
        List<Long> alunosIds
) {}