package com.israelmessias.minhasfinancas.service.impl;

import com.israelmessias.minhasfinancas.exception.ErroAutenticacao;
import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.model.repository.UsuarioRepository;
import com.israelmessias.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder encoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        super();
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    @Override
    public Usuario autenticar(String email, String senha)
    {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(!usuario.isPresent())
        {
            throw new ErroAutenticacao("Usuario não encontrado para o email informado.");
        }
        /*O metodo matches recebe a senha criptografada do banco de dados
        * e compara com a senha passada pelo usuario*/
        boolean senhasCorrespodem = encoder.matches(senha, usuario.get().getSenha());

        if(!senhasCorrespodem)
        {
            throw new ErroAutenticacao("Senha invalida!");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario)
    {
        validarEmail(usuario.getEmail());
        criptografarSenha(usuario);
        return usuarioRepository.save(usuario);
    }

    private void criptografarSenha(Usuario usuario)
    {
        String senhaCripto = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCripto);
    }

    @Override
    public void validarEmail(String email)
    {
        boolean existe = usuarioRepository.existsByEmail(email);
        if(existe)
        {
            throw new RegraNegocioException("Ja existe um usuario cadastrado!");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
