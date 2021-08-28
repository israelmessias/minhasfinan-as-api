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
    LancamentoRepository lancamentoRepository;

    public LancamentoServiceImpl (LancamentoRepository lancamentoRepository)
    {
        this.lancamentoRepository = lancamentoRepository;
    }

    //Salva os lançamentos
    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento)
    {
        return lancamentoRepository.save(lancamento);
    }

    //Salva os lançamentos que venham conter um objeto
    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento)
    {
        Objects.requireNonNull(lancamento.getId());
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return lancamentoRepository.save(lancamento);
    }

    //Deleta o objeto
    @Override
    @Transactional
    public void deletar(Lancamento lancamento)
    {
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        lancamentoRepository.delete(lancamento);
    }

    //Buscas
    @Override
    @Transactional(readOnly = true)//transação de leitura
    public List<Lancamento> buscar(Lancamento lancamentoFiltro)
    {
        Example example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching().withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return lancamentoRepository.findAll(example);
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
    public void validar(Lancamento lancamento)
    {
        if(lancamento.getDescricao()==null || lancamento.getDescricao().trim().equals(""))
            throw new RegraNegocioException("Informe uma descrição valida!");

        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes()>12)
            throw new RegraNegocioException("Informe mês valido!");

        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4)
            throw new RegraNegocioException("Informe ano valido!");

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null)
            throw new RegraNegocioException("Informe um Usuario valido!");

        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1)
            throw new RegraNegocioException("Informe um Valor valido!");

        if(lancamento.getTipo() == null)
            throw new RegraNegocioException("Informe um tipo valido!");
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return lancamentoRepository.findById(id);
    }
}
