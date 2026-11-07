package br.com.sup08.aluno.controller;

import br.com.sup08.aluno.dto.AlunoCreateRequest;
import br.com.sup08.aluno.dto.AlunoResponse;
import br.com.sup08.aluno.dto.AlunoUpdateRequest;
import br.com.sup08.aluno.service.AlunoService;
import br.com.sup08.matricula.service.MatriculaService;
import br.com.sup08.turma.dto.TurmaResponse;            // <-- IMPORT NOVO AQUI
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;                                  // <-- IMPORT NOVO AQUI

@RestController
@RequestMapping("/api/v1/alunos")
public class AlunoController {

    private final AlunoService service;
    private final MatriculaService matriculaService; // <-- 1. Adicionando a dependência aqui

    // <-- 2. Atualizando o construtor para receber as duas dependências
    public AlunoController(AlunoService service, MatriculaService matriculaService) {
        this.service = service;
        this.matriculaService = matriculaService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@RequestBody @Valid AlunoCreateRequest request) {
        AlunoResponse response = service.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> editar(@PathVariable Long id, @RequestBody @Valid AlunoUpdateRequest request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @GetMapping
    public ResponseEntity<Page<AlunoResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, cpf, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdResponse(id));
    }

    // <-- 3. O método de listar turmas agora está funcional!
    @GetMapping("/{id}/turmas")
    public ResponseEntity<List<TurmaResponse>> listarTurmas(@PathVariable Long id) {
        service.buscarPorIdResponse(id); // Valida se o aluno existe antes de buscar as turmas
        return ResponseEntity.ok(matriculaService.listarTurmasDoAluno(id));
    }
}