package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UsuarioService {
    //Metodo para autenticar a entrada do usuario no sistema
    Usuario autenticar(String email, String senha);

    //Salvar usuario
    Usuario salvarUsuario(Usuario usuario);

    //Para que o email só cadastre uma vez só
    void validarEmail(String email);

    Optional<Usuario> obterPorId(Long id);
}


