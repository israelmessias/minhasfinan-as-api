package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Lancamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {
}
