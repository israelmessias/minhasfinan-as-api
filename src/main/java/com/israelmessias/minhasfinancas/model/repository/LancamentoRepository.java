package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
