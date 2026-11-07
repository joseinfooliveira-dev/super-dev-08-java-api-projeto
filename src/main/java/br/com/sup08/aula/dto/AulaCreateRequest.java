package br.com.sup08.aula.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AulaCreateRequest(
        @NotNull(message = "A data/hora de início é obrigatória")
        LocalDateTime inicio,

        @NotNull(message = "A data/hora de fim é obrigatória")
        LocalDateTime fim,

        @NotNull(message = "O ID da turma é obrigatório")
        Long turmaId
) {}