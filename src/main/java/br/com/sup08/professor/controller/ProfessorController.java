package br.com.sup08.professor.controller;

import br.com.sup08.professor.domain.StatusProfessor;
import br.com.sup08.professor.dto.ProfessorCreateRequest;
import br.com.sup08.professor.dto.ProfessorResponse;
import br.com.sup08.professor.dto.ProfessorUpdateRequest;
import br.com.sup08.professor.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> cadastrar(@RequestBody @Valid ProfessorCreateRequest request) {
        ProfessorResponse response = service.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponse> editar(@PathVariable Long id, @RequestBody @Valid ProfessorUpdateRequest request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<ProfessorResponse> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.inativar(id));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<ProfessorResponse> reativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.reativar(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProfessorResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) StatusProfessor status,
            Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdResponse(id));
    }

    @GetMapping("/{id}/turmas")
    public ResponseEntity<?> listarTurmas(@PathVariable Long id) {
        service.buscarPorIdResponse(id);
        // TODO: Implementar busca no Módulo 4 (Turmas).
        return ResponseEntity.ok(java.util.List.of());
    }
}