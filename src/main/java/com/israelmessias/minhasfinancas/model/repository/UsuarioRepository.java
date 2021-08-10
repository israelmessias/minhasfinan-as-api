package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
