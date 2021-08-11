package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /*Abstração criada para buscar por email
    * por convenção findBy vai buscar algum elemento do banco de dados*/
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
