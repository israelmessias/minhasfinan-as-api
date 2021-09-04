package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
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

    private Lancamento criarLancamentos() {
        return Lancamento.builder().
                ano(2019).mes(9).
                descricao("Lançamento qualquer").
                valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now()).build();
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
        Lancamento lancamento = criarLancamentos();
        entityManager.persist(lancamento);

       lancamento =  entityManager.find(Lancamento.class, lancamento.getId());

       lancamentoRepository.delete(lancamento);
       Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
       Assertions.assertThat(lancamentoInexistente).isNull();
    }
}
