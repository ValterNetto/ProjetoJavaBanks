package com.testeJavaDeveloper.JavaBanks.controller;

import com.testeJavaDeveloper.JavaBanks.model.Empresas;
import com.testeJavaDeveloper.JavaBanks.repository.EmpresasRepository;
import com.testeJavaDeveloper.JavaBanks.verificate.VerificadorCnpj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresasController {

    @Autowired
    private EmpresasRepository empresasRepository;

    @GetMapping
    public List<Empresas> listar() { return empresasRepository.findAll(); }

    @PostMapping
    public ResponseEntity<String> adicionar(@RequestBody Empresas empresas) {
        VerificadorCnpj verificadorCnpj = new VerificadorCnpj();
        if(!verificadorCnpj.cnpjValido(empresas.getCnpj())){
            return ResponseEntity.badRequest().body("CNPJ inválido!");
        }

        try {
            empresas.setSaldo(0.0);
            empresas.setTaxas(0.0);
            empresasRepository.save(empresas);
            return ResponseEntity.ok("Empresa cadastrada com sucesso!");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar empresa!");
        }
    }
}
