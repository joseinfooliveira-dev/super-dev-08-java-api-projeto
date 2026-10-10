package br.com.sup08.unidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UnidadeCreateRequest(
        @NotBlank(message = "O nome é obrigatorio")
        @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "A sigla é obrigatória")
        @Size(min = 2, max = 10, message = "A sigla deve conter entre 2 e 10 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "A sigla deve conter apenas caracteres alfanuméricos")
        String sigla
){
}
