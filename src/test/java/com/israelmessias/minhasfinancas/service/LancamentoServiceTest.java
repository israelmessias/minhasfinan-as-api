package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.enums.StatusLancamento;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepository;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.israelmessias.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest
{
    @Autowired
    LancamentoRepositoryTest lancamentoRepositoryTest;
    @SpyBean
    LancamentoServiceImpl service;
    @MockBean
    LancamentoRepository repository;

    @Test
    public void salvarLancamento()
    {
        //cenario
        Lancamento lancamentoASalvar = lancamentoRepositoryTest.criarLancamentos();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = lancamentoRepositoryTest.criarLancamentos();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execução
        Lancamento lancamento = service.salvar(lancamentoASalvar);

        //
        org.assertj.core.api.Assertions.
                assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        org.assertj.core.api.Assertions.
                assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }
}
