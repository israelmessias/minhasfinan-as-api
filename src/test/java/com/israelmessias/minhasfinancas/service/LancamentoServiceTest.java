package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.model.enums.StatusLancamento;
import com.israelmessias.minhasfinancas.model.enums.TipoLancamento;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepository;
import com.israelmessias.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.israelmessias.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void naoSalvarUmLancamento()
    {
        //cenario
        Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamentos();
        Mockito.doThrow( RegraNegocioException.class ).when(service).validar(lancamentoSalvar);

        //execução e verigficação
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

        assertThat(result)
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
        assertThat(lancamento.getStatus()).isEqualTo(statusLancamento);
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
        assertThat(result.isPresent()).isTrue();
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
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void deveLancarErrosAoValidarUmLancamento() {
        Lancamento lancamento = new Lancamento();

        Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("");

        erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("Salario");

        erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setAno(0);

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setAno(13);

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(1);

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(202);

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(2020);

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

        lancamento.setUsuario(new Usuario());

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

        lancamento.getUsuario().setId(1l);

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.ZERO);

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.valueOf(1));

        erro = catchThrowable( () -> service.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lançamento.");

    }
}
