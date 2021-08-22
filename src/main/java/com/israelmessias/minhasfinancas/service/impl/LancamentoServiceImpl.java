package com.israelmessias.minhasfinancas.service.impl;

import com.israelmessias.minhasfinancas.model.Entity.Lancamentos;
import com.israelmessias.minhasfinancas.model.Entity.StatusLancamento;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepository;
import com.israelmessias.minhasfinancas.service.LancamentoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoServiceImpl implements LancamentoService
{
    LancamentoRepository lancamentoRepository;

    public LancamentoServiceImpl (LancamentoRepository lancamentoRepository)
    {
        this.lancamentoRepository = lancamentoRepository;
    }

    @Override
    public Lancamentos salvar(Lancamentos lancamentos)
    {
        return null;
    }

    @Override
    public Lancamentos atualizar(Lancamentos lancamentos)
    {
        return null;
    }

    @Override
    public void deletar(Lancamentos lancamentos)
    {
    }

    @Override
    public List<Lancamentos> buscar(Lancamentos lancamentoFiltro)
    {
        return null;
    }

    @Override
    public void atualizarStatus(Lancamentos lancamentos, StatusLancamento status)
    {
    }
}
