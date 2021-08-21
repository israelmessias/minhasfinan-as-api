package com.israelmessias.minhasfinancas.service;

import com.israelmessias.minhasfinancas.exception.ErroAutenticacao;
import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.model.repository.UsuarioRepository;
import com.israelmessias.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest
{
    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository usuarioRepository;

/******************************************** VALIDAÇÕES ***************************************************************/
    /*testa o metodo deveValida do service
    *
    *Mockito.when"Simula quando"
    * quando for buscar um email de qualquer string vai retornar falso e evita a exceção
    *  -> (usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false)
    * service.validarEmail("email@email.com") False*/
    @Test
    public void deveValidarEmail()
    {
        //cenario
        //usuarioRepository.deleteAll(); Teste unitario
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //ação
        service.validarEmail("email@email.com");
    }

    /*Lança o erro abaixo quando o email tiver cadastrado:
            RegraNegocioException(String msg) */

    @Test
    public void deveLancarErroAoValidarEmailQuandoCadastrado()
    {
        Assertions.assertThrows(RegraNegocioException.class, ()->
        {
            Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

            //ação
            service.validarEmail("email@email.com");
        });
    }

    /*****************************AUTENTICAÇÕES***************************/

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado()
    {
        //cenario
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());//retorna vazio

        //
        Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() ->
                service.autenticar("email@email.com", "senha"));

        org.assertj.core.api.Assertions.assertThat(exception)
                .isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario não encontrado para o email informado.");
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso()
    {
        //cenario
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email)
                .senha(senha).id(1L).build();
        Mockito.when(usuarioRepository.findByEmail(email) ).thenReturn(Optional.of(usuario));

        //ação
        Usuario result = service.autenticar(email, senha);

        //verificação
        org.assertj.core.api.Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComSenhaInformado()
    {
            String senha = "senha";
            Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
            Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

            //ação
            Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() ->
                    service.autenticar("email@email.com", "123"));
            org.assertj.core.api.Assertions.assertThat(exception)
                    .isInstanceOf(ErroAutenticacao.class).hasMessage("Senha invalida!");
    }

    /*************************************************SALVAR***********************************************************/
    @Test
    public void deveSalvarUsuario(){
        // cenario:Não fazer nada quando mockar validarEmail
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());

        Usuario usuario = Usuario.builder().id(1L).nome("usuario").
                email("ususario@usuario.com").senha("senha").build();

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class) ))
                .thenReturn(usuario);
        //ação
        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        //Verificação: Verifica o usuario do cenario
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("ususario@usuario.com");
        org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }
    @Test
    public void naoDeveSalvarUsuario()
    {
        //cenario
        //cenario
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        //acao
        org.junit.jupiter.api.Assertions
                .assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario) ) ;

        //verificacao
        Mockito.verify( usuarioRepository, Mockito.never() ).save(usuario);
    }
}
