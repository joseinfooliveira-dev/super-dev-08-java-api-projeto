package br.com.sup08.unidade.dto;

import br.com.sup08.unidade.domain.Unidade;
import java.time.OffsetDateTime;

public record UnidadeResponse(
        Long id,
        String nome,
        String sigla,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static UnidadeResponse fromEntity(Unidade u) {
        return new UnidadeResponse(u.getId(), u.getNome(), u.getSigla(), u.getCreatedAt(), u.getUpdatedAt());
    }
}