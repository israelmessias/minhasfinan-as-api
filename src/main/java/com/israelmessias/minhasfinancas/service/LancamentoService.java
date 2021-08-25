package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.Entity.StatusLancamento;

import java.util.List;

public interface LancamentoService
{
    Lancamento salvar (Lancamento lancamento);

    Lancamento atualizar (Lancamento lancamento);
    void deletar (Lancamento lancamento);

    List<Lancamento> buscar (Lancamento lancamentoFiltro);

    void atualizarStatus (Lancamento lancamento, StatusLancamento status);

    void validar (Lancamento lancamento);
}
