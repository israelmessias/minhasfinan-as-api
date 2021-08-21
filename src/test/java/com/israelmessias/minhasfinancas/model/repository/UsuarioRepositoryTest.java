package com.israelmessias.minhasfinancas.model.repository;

import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //Cenario
        Usuario usuario1 = Usuario.builder().nome("israel").email("israel@email.com").build();
        usuarioRepository.save(usuario1);
        //Execução
        boolean result = usuarioRepository.existsByEmail("israel@email.com");
        //verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoNaoHouverQuandoUsuarioForCadastradoComEmail(){
        //cenario
        usuarioRepository.deleteAll();

        //ação
        boolean result = usuarioRepository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isFalse();
    }
}
