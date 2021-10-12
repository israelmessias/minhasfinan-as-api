package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import com.israelmessias.minhasfinancas.model.enums.StatusLancamento;
import com.israelmessias.minhasfinancas.model.enums.TipoLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    /*
    * Vai somar todos lançamentos agrupados pelo tipo de usuario*/
    @Query(value = "select sum(l.valor) from Lancamento l join l.usuario u " +
            "where u.id = :idUsuario and l.tipo =:tipo and l.status = :status group by u")
    BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(
        @Param("idUsuario") Long idUsuario, 
        @Param("tipo") TipoLancamento tipo,
        @Param("status") StatusLancamento status);
}
