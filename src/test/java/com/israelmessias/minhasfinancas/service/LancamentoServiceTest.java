package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.enums.StatusLancamento;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepository;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.israelmessias.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest
{
    @SpyBean
    LancamentoServiceImpl service;
    @MockBean
    LancamentoRepository repository;

    @Test
    public void salvarLancamento()
    {
        //cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamentos();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamentos();
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

    @Test
    public void naoSalvarUmLancamento()
    {
        //cenario
        Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamentos();
        Mockito.doThrow( RegraNegocioException.class ).when(service).validar(lancamentoSalvar);

        //execução e verigficação
        org.assertj.core.api.Assertions.
                catchThrowableOfType( () -> service.salvar(lancamentoSalvar), RegraNegocioException.class );
        Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
    }

    @Test
    public void atualizarLancamento() {
        //cenario
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamentos();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(service).validar(lancamentoSalvo);

        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //execução
        Lancamento lancamento = service.atualizar(lancamentoSalvo);

        //
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void erroAoTentarAtualizar()
    {
        //cenario
        Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamentos();

        //execução e verigficação
        org.assertj.core.api.Assertions.
                catchThrowableOfType( () -> service.atualizar(lancamentoSalvar), NullPointerException.class );
        Mockito.verify(repository, Mockito.never()).save(lancamentoSalvar);
    }

    @Test
    public void deletarUmLancamento()
    {
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamentos();
        lancamento.setId(1L);

        //execução
        service.deletar(lancamento);

        Mockito.verify(repository).delete(lancamento);
    }

    @Test
    public void deletarLancamentoQueNaoExiste()
    {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamentos();

        //execução
        org.assertj.core.api.Assertions.
                catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class );

        Mockito.verify(repository, Mockito.never()).delete(lancamento);
    }

    @Test
    public void filtrarLancamento()
    {
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamentos();
        lancamento.setId(1L);

        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(repository.findAll(Mockito.any(Example.class)) ).thenReturn(lista);

        //execução
        List<Lancamento> result = service.buscar(lancamento);

        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(lancamento);

    }

    @Test
    public void atualizarOStatusDeLancamento()
    {
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamentos();
        lancamento.setId(1L);

        StatusLancamento statusLancamento = StatusLancamento.PENDENTE;
        //Ele não vai chegar a parte de atualização do metodo
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        //execução
        service.atualizarStatus(lancamento, statusLancamento);

        //verificação
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(statusLancamento);
        Mockito.verify(service).atualizar(lancamento);
    }

    @Test
    public void obterLancamentoPorId()
    {
        //cenario
        Long id = 1L;
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamentos();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        //execução
        Optional<Lancamento> result = service.obterPorId(id);

        //verificação
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void retornarVazioLancamentoPorId()
    {
        //cenario
        Long id = 1L;
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamentos();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Lancamento> result = service.obterPorId(id);

        //verificação
        Assertions.assertThat(result.isPresent()).isFalse();
    }
}
