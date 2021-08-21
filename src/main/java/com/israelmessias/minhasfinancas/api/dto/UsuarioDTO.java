package com.israelmessias.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
}
