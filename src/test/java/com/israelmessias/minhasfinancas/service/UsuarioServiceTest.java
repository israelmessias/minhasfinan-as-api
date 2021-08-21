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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
    //Não necessita do @Autowired 'Dependency Injection'.
    UsuarioServiceImpl service;
    @MockBean
    UsuarioRepository usuarioRepository;

    /*Chamada falsa no repository -> repository = Mockito...
    O service
    * Chamada verdadeira no service -> service = new Service(repository)*/
    @BeforeEach
    public void setup()
    {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        service = new UsuarioServiceImpl(usuarioRepository);
    }
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
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado()
    {
        Assertions.assertThrows(ErroAutenticacao.class, ()->
        {
            //cenario
            Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());//retorna vazio

            //
            service.autenticar("email@email.com", "senha");
        });
    }
    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComSenhaInformado()
    {
        Assertions.assertThrows(ErroAutenticacao.class, ()->
        {
            String senha = "senha";
            Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
            Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

            //ação
            service.autenticar("email@email.com", "123");
        });
    }
}
