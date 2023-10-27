package com.testeJavaDeveloper.JavaBanks.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Clientes {

    private Long empresa;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "double default 0.0")
    private Long idcliente;

    private String nome;

    private String cpf;

    private String email;

    private String senha;

    @Column(columnDefinition = "double default 0.0")
    private double saldo = 0.0;
}
