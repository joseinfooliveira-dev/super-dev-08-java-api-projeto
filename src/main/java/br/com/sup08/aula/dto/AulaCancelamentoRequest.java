package br.com.sup08.aula.dto;

import jakarta.validation.constraints.NotBlank;

public record AulaCancelamentoRequest(
        @NotBlank(message = "O motivo do cancelamento é obrigatório")
        String motivo
) {}