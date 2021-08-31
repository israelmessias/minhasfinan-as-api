package com.israelmessias.minhasfinancas.service.impl;

import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.Entity.StatusLancamento;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepository;
import com.israelmessias.minhasfinancas.service.LancamentoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService
{
    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository)
    {
        this.repository = lancamentoRepository;
    }

    //Salva os lançamentos
    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento)
    {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    //Salva os lançamentos que venham conter um objeto
    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento)
    {
        Objects.requireNonNull(lancamento.getId());
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    //Deleta o objeto
    @Override
    @Transactional
    public void deletar(Lancamento lancamento)
    {
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        repository.delete(lancamento);
    }

    //Buscas
    @Override
    @Transactional(readOnly = true)//transação de leitura
    public List<Lancamento> buscar(Lancamento lancamentoFiltro)
    {
        Example example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching().withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    @Transactional
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status)
    {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    // Valida descrições para não serem vazias ou so com space
    @Override
    public void validar(Lancamento lancamento) {

        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma Descrição válida.");
        }

        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe um Mês válido.");
        }

        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4 ) {
            throw new RegraNegocioException("Informe um Ano válido.");
        }

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um Usuário.");
        }

        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
            throw new RegraNegocioException("Informe um Valor válido.");
        }

        if(lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de Lançamento.");
        }
    }
    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return repository.findById(id);
    }
}
