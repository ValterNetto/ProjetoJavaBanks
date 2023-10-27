package com.testeJavaDeveloper.JavaBanks.repository;

import com.testeJavaDeveloper.JavaBanks.model.Empresas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresasRepository extends JpaRepository<Empresas, Long> {
}
