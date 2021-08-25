package com.israelmessias.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
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
