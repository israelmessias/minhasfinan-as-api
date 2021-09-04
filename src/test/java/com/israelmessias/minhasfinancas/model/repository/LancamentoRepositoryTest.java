package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.model.enums.StatusLancamento;
import com.israelmessias.minhasfinancas.model.enums.TipoLancamento;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest
{
    @Autowired
    LancamentoRepository lancamentoRepository;

    @Autowired
    TestEntityManager entityManager;

    public Lancamento criarLancamentos()
    {
        return Lancamento.builder().
                ano(2019).mes(9).
                descricao("Lançamento qualquer").
                valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now()).build();
    }

    private Lancamento criarEPersistirLancamento()
    {
        Lancamento lancamento = criarLancamentos();
        entityManager.persist(lancamento);
        return lancamento;
    }

    @Test
    public void  deveSalvarUmLancamento()
    {
        Lancamento lancamento = criarLancamentos();

        lancamento = lancamentoRepository.save(lancamento);

        Assertions.assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void  deveDeletarUmLancamento()
    {
        Lancamento lancamento = criarEPersistirLancamento();

        lancamento =  entityManager.find(Lancamento.class, lancamento.getId());

       lancamentoRepository.delete(lancamento);
       Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
       Assertions.assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void atualizarLancamento()
    {
        Lancamento lancamento = criarEPersistirLancamento();

        lancamento.setAno(2020);
        lancamento.setDescricao("Teste Atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);
        lancamentoRepository.save(lancamento);

        //A classe = tabela, depois a primary key
        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
        Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
        Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
        Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void buscarId()
    {
        Lancamento lancamento = criarEPersistirLancamento();

        Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(lancamento.getId());

        Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }
}
