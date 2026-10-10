package br.com.sup08.common.exception;

import java.util.List;

public record ErroResponse(
        String type,
        String title,
        int status,
        String codigo,
        String mensagem,
        List<CampoErro> campos
) {
    public record CampoErro(String campo, String mensagem) {}
}
