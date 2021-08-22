package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.model.Entity.Lancamentos;
import com.israelmessias.minhasfinancas.model.Entity.StatusLancamento;

import java.util.List;

public interface LancamentoService
{
    Lancamentos salvar (Lancamentos lancamentos);

    Lancamentos atualizar (Lancamentos lancamentos);
    void deletar (Lancamentos lancamentos);

    List<Lancamentos> buscar (Lancamentos lancamentoFiltro);

    void atualizarStatus (Lancamentos lancamentos, StatusLancamento status);
}
