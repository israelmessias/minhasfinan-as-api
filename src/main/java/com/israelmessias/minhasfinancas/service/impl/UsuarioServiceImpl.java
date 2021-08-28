package com.israelmessias.minhasfinancas.service.impl;

import com.israelmessias.minhasfinancas.exception.ErroAutenticacao;
import com.israelmessias.minhasfinancas.exception.RegraNegocioException;
import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.model.repository.UsuarioRepository;
import com.israelmessias.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        super();
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha)
    {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(!usuario.isPresent())
        {
            throw new ErroAutenticacao("Usuario não encontrado para o email informado.");
        }
        if(!usuario.get().getSenha().equals(senha))
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
        return usuarioRepository.save(usuario);
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
