package br.com.sup08.unidade.service;

import br.com.sup08.common.exception.RecursoNaoEncontradoException;
import br.com.sup08.common.exception.RegraNegocioException;
import br.com.sup08.unidade.domain.Unidade;
import br.com.sup08.unidade.dto.UnidadeCreateRequest;
import br.com.sup08.unidade.dto.UnidadeResponse;
import br.com.sup08.unidade.dto.UnidadeUpdateRequest;
import br.com.sup08.unidade.repository.UnidadeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnidadeService {

    private final UnidadeRepository repository;

    public UnidadeService(UnidadeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UnidadeResponse cadastrar(UnidadeCreateRequest request) {
        String nomeNormalizado = request.nome().trim();
        String siglaNormalizada = request.sigla().trim().toUpperCase();

        // RG0100: Não permite duplicidade de sigla (case-insensitive e trim)
        if (repository.existsBySiglaIgnoreCase(siglaNormalizada)) {
            throw new RegraNegocioException("RG0100", "Já existe uma unidade cadastrada com a sigla " + siglaNormalizada + ".");
        }

        // RG0103: Não permite duplicidade de nome (case-insensitive)
        if (repository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegraNegocioException("RG0103", "Já existe uma unidade cadastrada com o nome " + nomeNormalizado + ".");
        }

        Unidade unidade = new Unidade(nomeNormalizado, siglaNormalizada);
        Unidade salva = repository.save(unidade);
        return UnidadeResponse.fromEntity(salva);
    }

    @Transactional
    public UnidadeResponse editar(Long id, UnidadeUpdateRequest request) {
        Unidade unidade = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade com ID " + id + " não encontrada."));

        // RG0101: A sigla é imutável. Se veio sigla diferente da atual, rejeita com 409
        if (request.sigla() != null && !request.sigla().trim().equalsIgnoreCase(unidade.getSigla())) {
            throw new RegraNegocioException("RG0101", "A sigla da unidade não pode ser alterada.");
        }

        String nomeNormalizado = request.nome().trim();

        // RG0103: Nome único no update
        if (repository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RegraNegocioException("RG0103", "Já existe outra unidade cadastrada com o nome " + nomeNormalizado + ".");
        }

        unidade.setNome(nomeNormalizado);
        return UnidadeResponse.fromEntity(unidade);
    }

    @Transactional(readOnly = true)
    public UnidadeResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(UnidadeResponse::fromEntity)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade com ID " + id + " não encontrada."));
    }

    @Transactional(readOnly = true)
    public Page<UnidadeResponse> listar(String nome, String sigla, Pageable pageable) {
        return repository.pesquisar(nome, sigla, pageable).map(UnidadeResponse::fromEntity);
    }
}