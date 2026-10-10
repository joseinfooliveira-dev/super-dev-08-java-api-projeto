package br.com.sup08.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

        //RNF05: Violação de regra de negocio devolve 409 Conflict com código
        //Exemplo: (RG0100)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse>
handleRegraNegocio(RegraNegocioException ex) {
        ErroResponse erro = new ErroResponse(
                "https://api.sup08.local/erros/regra-de-negocio",
                "Violação de Regra de Negócios",
                HttpStatus.CONFLICT.value(),
                ex.getCodigo(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    // RNF04: Recurso não encontrado devolve error 404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse>
handleNaoEncontrado(RecursoNaoEncontradoException exception) {
        ErroResponse erro = new ErroResponse(
                "https://api.sup08.local/errors/recurso-nao-encontrado",
                "Recurso Não Encontrado, tente novamente ou chame o Lorenzo",
                HttpStatus.NOT_FOUND.value(),
                null,
                exception.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // RNF03/RNF04: Erro de campo inválido devolve 400 BAD REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse>
handleValidacao(MethodArgumentNotValidException ex) {
        List<ErroResponse.CampoErro> campos =
ex.getBindingResult().getFieldErrors().stream()
        .map(f -> new ErroResponse.CampoErro(f.getField(),
f.getDefaultMessage()))
        .toList();

    ErroResponse erro = new ErroResponse(
            "https://api.sup08.local/erros/validacao",
            "Erro de validação de dados, veja com Lorenzo pra te ajudar",
            HttpStatus.BAD_REQUEST.value(),
            null,
            "Um ou mais campos contêm valores inválidos.",
            campos
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
