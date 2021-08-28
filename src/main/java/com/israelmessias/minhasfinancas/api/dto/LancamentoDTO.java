package com.israelmessias.minhasfinancas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LancamentoDTO {
    private Long id;

    private String descriçao;

    private Integer mes;

    private Integer ano;

    private BigDecimal valor;

    private Long Usuario;

    private String tipo;

    private String status;

}
