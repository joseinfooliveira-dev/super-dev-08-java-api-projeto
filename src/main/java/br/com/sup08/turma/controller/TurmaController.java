package br.com.sup08.turma.controller;

import br.com.sup08.turma.domain.StatusTurma;
import br.com.sup08.turma.dto.*;
import br.com.sup08.turma.service.TurmaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/turmas")
public class TurmaController {

    private final TurmaService service;

    public TurmaController(TurmaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TurmaResponse> cadastrar(@RequestBody @Valid TurmaCreateRequest request) {
        TurmaResponse response = service.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponse> editar(@PathVariable Long id, @RequestBody @Valid TurmaUpdateRequest request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<TurmaResponse> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.inativar(id));
    }

    @GetMapping
    public ResponseEntity<Page<TurmaResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long professorId,
            @RequestParam(required = false) Long unidadeId,
            @RequestParam(required = false) StatusTurma status,
            Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, professorId, unidadeId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdResponse(id));
    }

    @GetMapping("/{id}/progresso")
    public ResponseEntity<TurmaProgressoResponse> obterProgresso(@PathVariable Long id) {
        return ResponseEntity.ok(service.obterProgresso(id));
    }
}