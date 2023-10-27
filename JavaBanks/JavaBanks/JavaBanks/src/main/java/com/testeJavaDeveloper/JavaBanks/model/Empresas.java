package com.testeJavaDeveloper.JavaBanks.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Empresas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idempresa;

    private String nome;

    private String cnpj;

    private String email;

    private String senha;

    @Column(columnDefinition = "double default 0.0")
    private double saldo = 0.0;

    @Column(columnDefinition = "double default 0.0")
    private double taxas = 0.0;
}
