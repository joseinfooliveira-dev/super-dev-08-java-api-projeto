package br.com.sup08.matricula.controller;

import br.com.sup08.aluno.dto.AlunoResponse;
import br.com.sup08.matricula.dto.MatriculaLoteRequest;
import br.com.sup08.matricula.dto.MatriculaResponse;
import br.com.sup08.matricula.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/turmas") // Mudou para o padrão exigido
public class MatriculaController {

    private final MatriculaService service;

    public MatriculaController(MatriculaService service) {
        this.service = service;
    }

    // RF0500 e RF0501: Aceita matrícula única ou em lote na mesma rota
    @PostMapping("/{turmaId}/alunos")
    public ResponseEntity<List<MatriculaResponse>> matricular(
            @PathVariable Long turmaId,
            @RequestBody @Valid MatriculaLoteRequest request) {

        // Para manter compatibilidade com matrícula única, a service já lida com listas
        MatriculaLoteRequest req = new MatriculaLoteRequest(turmaId, request.alunosIds());
        return ResponseEntity.ok(service.matricularLote(req));
    }

    // RF0502: Remover matrícula
    @DeleteMapping("/{turmaId}/alunos/{alunoId}")
    public ResponseEntity<Void> remover(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        service.remover(alunoId, turmaId);
        return ResponseEntity.noContent().build();
    }

    // RF0503: Listar alunos matriculados
    @GetMapping("/{turmaId}/alunos")
    public ResponseEntity<Page<AlunoResponse>> listarAlunosDaTurma(@PathVariable Long turmaId, Pageable pageable) {
        return ResponseEntity.ok(service.listarAlunosDaTurma(turmaId, pageable));
    }

    // RF0504 (listar turmas do aluno) continua no AlunoController como planejado
}