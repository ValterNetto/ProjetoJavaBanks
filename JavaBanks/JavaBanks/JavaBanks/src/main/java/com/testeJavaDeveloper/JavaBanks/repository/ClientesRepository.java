package com.testeJavaDeveloper.JavaBanks.repository;

import com.testeJavaDeveloper.JavaBanks.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Long > {

}
