package br.com.sup08.unidade.controller;

import br.com.sup08.unidade.dto.UnidadeCreateRequest;
import br.com.sup08.unidade.dto.UnidadeResponse;
import br.com.sup08.unidade.dto.UnidadeUpdateRequest;
import br.com.sup08.unidade.service.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/unidades")
public class UnidadeController {

    private final UnidadeService service;

    public UnidadeController(UnidadeService service) {
        this.service = service;
    }

    // RF0100: Cadastrar (201 Created + Header Location conforme RNF04)
    @PostMapping
    public ResponseEntity<UnidadeResponse> cadastrar(@RequestBody @Valid UnidadeCreateRequest request) {
        UnidadeResponse response = service.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    // RF0101: Editar (200 OK)
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> editar(@PathVariable Long id, @RequestBody @Valid UnidadeUpdateRequest request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    // RF0103: Buscar por ID (200 OK)
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // RF0102 e RF0104: Listar com filtros e paginação
    @GetMapping
    public ResponseEntity<Page<UnidadeResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sigla,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.listar(nome, sigla, pageable));
    }
}