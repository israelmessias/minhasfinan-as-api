package com.israelmessias.minhasfinancas.service.impl;

import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Lancamentos;
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
    public Lancamentos salvar(Lancamentos lancamentos)
    {
        return lancamentoRepository.save(lancamentos);
    }

    //Salva os lançamentos que venham conter um objeto
    @Override
    @Transactional
    public Lancamentos atualizar(Lancamentos lancamentos)
    {
        Objects.requireNonNull(lancamentos.getId());
        return lancamentoRepository.save(lancamentos);
    }

    //Deleta o objeto
    @Override
    @Transactional
    public void deletar(Lancamentos lancamentos)
    {
        Objects.requireNonNull(lancamentos.getId());
        lancamentoRepository.delete(lancamentos);
    }

    //Buscas
    @Override
    @Transactional(readOnly = true)//transação de leitura
    public List<Lancamentos> buscar(Lancamentos lancamentoFiltro)
    {
        Example example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching().withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return lancamentoRepository.findAll(example);
    }

    @Override
    @Transactional
    public void atualizarStatus(Lancamentos lancamentos, StatusLancamento status)
    {
        lancamentos.setStatus(status);
        atualizar(lancamentos);
    }

    // Valida descrições para não serem vazias ou so com space
    @Override
    public void validar(Lancamentos lancamentos)
    {
        if(lancamentos.getDescricao()==null || lancamentos.getDescricao().trim().equals(""))
            throw new RegraNegocioException("Informe uma descrição valida!");

        if(lancamentos.getMes() == null || lancamentos.getMes() < 1 || lancamentos.getMes()>12)
            throw new RegraNegocioException("Informe mês valido!");

        if(lancamentos.getAno() == null || lancamentos.getAno().toString().length() != 4)
            throw new RegraNegocioException("Informe ano valido!");

        if(lancamentos.getUsuario() == null || lancamentos.getUsuario().getId() == null)
            throw new RegraNegocioException("Informe um Usuario valido!");

        if(lancamentos.getValor() == null || lancamentos.getValor().compareTo(BigDecimal.ZERO) < 1)
            throw new RegraNegocioException("Informe um Valor valido!");

        if(lancamentos.getTipo() == null)
            throw new RegraNegocioException("Informe um tipo valido!");
    }
}
