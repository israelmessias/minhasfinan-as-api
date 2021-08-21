package com.israelmessias.minhasfinancas.model.repository;

import java.util.Optional;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //Cenario
        Usuario usuario1 = criarUsuario();
        entityManager.persist(usuario1);
        //Execução
        boolean result = usuarioRepository.existsByEmail("usuario@email.com");
        //verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoNaoHouverQuandoUsuarioForCadastradoComEmail(){
        //cenario
        //usuarioRepository.deleteAll();

        //ação
        boolean result = usuarioRepository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUsuarioNoBD(){
        Usuario usuario = criarUsuario();

        //
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUsuarioPorEmail(){
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //verificação
        Optional<Usuario>result = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat( result.isPresent() ).isTrue();
    }

    @Test
    public void deveRetornarVazioAoUsuarioPorEmailQuandoNaoExisteBD(){
        //Usuario usuario = criarUsuario();
        //entityManager.persist(usuario);

        //verificação
        Optional<Usuario>result = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat( result.isPresent() ).isFalse();
    }

    public static Usuario criarUsuario(){
        return Usuario.builder()
        .nome("usuario").email("usuario@email.com")
        .senha("12345").build();
    }
}
