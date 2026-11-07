package br.com.sup08.aluno.service;

import br.com.sup08.aluno.domain.Aluno;
import br.com.sup08.aluno.dto.AlunoCreateRequest;
import br.com.sup08.aluno.dto.AlunoResponse;
import br.com.sup08.aluno.dto.AlunoUpdateRequest;
import br.com.sup08.aluno.repository.AlunoRepository;
import br.com.sup08.common.exception.RecursoNaoEncontradoException;
import br.com.sup08.common.exception.RegraNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AlunoResponse cadastrar(AlunoCreateRequest request) {
        String cpfLimpo = request.cpf().replaceAll("\\D", "");

        if (!isCpfValido(cpfLimpo)) {
            throw new RegraNegocioException("RG0301", "CPF inválido");
        }

        if (repository.existsByCpf(cpfLimpo)) {
            throw new RegraNegocioException("RG0300", "CPF já cadastrado");
        }

        validarDataNascimento(request.dataNascimento());

        Aluno aluno = new Aluno(request.nome(), cpfLimpo, request.dataNascimento());
        aluno = repository.save(aluno);
        return AlunoResponse.fromEntity(aluno);
    }

    @Transactional
    public AlunoResponse editar(Long id, AlunoUpdateRequest request) {
        Aluno aluno = buscarPorId(id);
        validarDataNascimento(request.dataNascimento());

        aluno.setNome(request.nome());
        aluno.setDataNascimento(request.dataNascimento());
        // CPF não é alterado (RG0302)

        aluno = repository.save(aluno);
        return AlunoResponse.fromEntity(aluno);
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponse> listar(String nome, String cpf, Pageable pageable) {
        String cpfLimpo = (cpf != null && !cpf.isBlank()) ? cpf.replaceAll("\\D", "") : null;
        return repository.buscarComFiltros(nome, cpfLimpo, pageable)
                .map(AlunoResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorIdResponse(Long id) {
        return AlunoResponse.fromEntity(buscarPorId(id));
    }

    private Aluno buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com o id: " + id));
    }

    private void validarDataNascimento(LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();

        if (dataNascimento.isAfter(hoje)) {
            throw new RegraNegocioException("RG0303", "Data de nascimento não pode ser futura");
        }

        int idade = Period.between(dataNascimento, hoje).getYears();

        if (idade > 120) {
            throw new RegraNegocioException("RG0303", "Data de nascimento não pode ser anterior a 120 anos");
        }

        if (idade < 16) {
            throw new RegraNegocioException("RG0304", "Aluno deve ter no mínimo 16 anos completos");
        }
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        try {
            int d1 = 0, d2 = 0;
            for (int i = 0; i < 9; i++) {
                d1 += (cpf.charAt(i) - '0') * (10 - i);
                d2 += (cpf.charAt(i) - '0') * (11 - i);
            }
            d1 = 11 - (d1 % 11);
            if (d1 >= 10) d1 = 0;
            d2 += d1 * 2;
            d2 = 11 - (d2 % 11);
            if (d2 >= 10) d2 = 0;
            return cpf.charAt(9) - '0' == d1 && cpf.charAt(10) - '0' == d2;
        } catch (Exception e) {
            return false;
        }
    }
}