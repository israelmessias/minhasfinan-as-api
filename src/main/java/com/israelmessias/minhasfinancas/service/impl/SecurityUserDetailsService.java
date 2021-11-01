package com.israelmessias.minhasfinancas.service.impl;

import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import com.israelmessias.minhasfinancas.model.repository.UsuarioRepository;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    public SecurityUserDetailsService(UsuarioRepository usuarioRepository)
    {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        Usuario usuarioAchado = usuarioRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("Email não encontrado!"));

        return  User.builder()
                .username(usuarioAchado.getEmail())
                .password(usuarioAchado.getSenha())
                .roles("USER").build();
    }
}
