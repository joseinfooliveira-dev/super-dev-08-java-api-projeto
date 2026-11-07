package br.com.sup08.aula.controller;

import br.com.sup08.aula.domain.StatusAula;
import br.com.sup08.aula.dto.*;
import br.com.sup08.aula.service.AulaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/aulas")
public class AulaController {

    private final AulaService service;

    public AulaController(AulaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AulaResponse> agendar(@RequestBody @Valid AulaCreateRequest request) {
        AulaResponse response = service.agendar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AulaResponse> editar(@PathVariable Long id, @RequestBody @Valid AulaUpdateRequest request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<AulaResponse> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(service.iniciar(id));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<AulaResponse> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(service.finalizar(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AulaResponse> cancelar(@PathVariable Long id, @RequestBody @Valid AulaCancelamentoRequest request) {
        return ResponseEntity.ok(service.cancelar(id, request));
    }

    @GetMapping
    public ResponseEntity<Page<AulaResponse>> listar(
            @RequestParam(required = false) Long turmaId,
            @RequestParam(required = false) Long professorId,
            @RequestParam(required = false) StatusAula status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            Pageable pageable) {
        return ResponseEntity.ok(service.listar(turmaId, professorId, status, inicio, fim, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdResponse(id));
    }

    @GetMapping("/professores/{professorId}/agenda")
    public ResponseEntity<List<AulaResponse>> obterAgenda(
            @PathVariable Long professorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(service.obterAgendaProfessor(professorId, inicio, fim));
    }
}