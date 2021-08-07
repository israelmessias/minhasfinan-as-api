package com.israelmessias.minhasfinancas.Entity;

import javax.persistence.*;

@Entity
@Table(name="lacamentos", schema = "financas")
public class Lancamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="mes")
    private Integer mes;

    @Column(name="ano")
    private Integer ano;

    private Usuario usuario;


}
