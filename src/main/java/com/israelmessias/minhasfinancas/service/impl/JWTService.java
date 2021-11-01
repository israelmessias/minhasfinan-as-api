package com.israelmessias.minhasfinancas.service.impl;

import com.israelmessias.minhasfinancas.model.Entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JWTService {
    String gerarToken(Usuario usuario);

    Claims obterClaim(String token) throws ExpiredJwtException;

    boolean istokenValido(String token);

    String obterLoginUsuario(String token);

    
}
