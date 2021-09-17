package com.israelmessias.minhasfinancas.api.dto;

import lombok.*;
import javax.validation.constraints.Email;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String nome;
    @Email
    private String email;
    private String senha;
}
