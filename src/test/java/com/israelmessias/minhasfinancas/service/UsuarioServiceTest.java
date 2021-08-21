package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.model.repository.UsuarioRepository;
import com.israelmessias.minhasfinancas.service.impl.UsuarioServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
    
    @Autowired
    UsuarioServiceImpl service;

    @Autowired
    UsuarioRepository usuarioRepository;

    /*testa o metodo deveValida do service*/

    @Test
    public void deveValidarEmail(){

        //cenario
        usuarioRepository.deleteAll();

        //ação
        service.validarEmail("email@email.com");
    }

    /*Lança o erro abaixo quando o email tiver cadastrado:
            public RegraNegocioException(String msg) {
                super(msg);
            }
    */
    @Test
    public void deveLancarErroAoValidarEmailQuandoCadastrado(){
        Assertions.assertThrows(RegraNegocioException.class, ()->{
        Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
        usuarioRepository.save(usuario);

        //ação
        service.validarEmail("email@email.com");
        });
    }
}
