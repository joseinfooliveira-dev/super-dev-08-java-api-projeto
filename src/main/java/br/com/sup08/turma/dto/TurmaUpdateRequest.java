package br.com.sup08.turma.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TurmaUpdateRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
        String nome,

        @NotNull(message = "A carga horária é obrigatória")
        @Min(value = 1, message = "A carga horária deve ser maior que 0")
        @Max(value = 9999, message = "A carga horária deve ser no máximo 9999")
        Integer cargaHoraria,

        @NotNull(message = "O ID do professor é obrigatório")
        Long professorId
) {}