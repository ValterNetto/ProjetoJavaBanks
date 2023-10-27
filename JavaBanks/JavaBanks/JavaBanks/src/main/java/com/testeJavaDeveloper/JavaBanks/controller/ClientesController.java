package com.testeJavaDeveloper.JavaBanks.controller;

import com.testeJavaDeveloper.JavaBanks.model.Clientes;
import com.testeJavaDeveloper.JavaBanks.model.Empresas;
import com.testeJavaDeveloper.JavaBanks.repository.ClientesRepository;
import com.testeJavaDeveloper.JavaBanks.repository.EmpresasRepository;
import com.testeJavaDeveloper.JavaBanks.service.EmailService;
import com.testeJavaDeveloper.JavaBanks.verificate.VerificadorCpf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private ClientesRepository clientesRepository;
    @Autowired
    private EmpresasRepository empresasRepository;
    @Autowired
    private EmailService emailService;


    String subject = "Transação concluída - Java Banks";

    @GetMapping
    public List<Clientes> listar() {
        return clientesRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody Clientes clientes) {
        VerificadorCpf verificadorCpf = new VerificadorCpf();
        if (!verificadorCpf.cpfValido(clientes.getCpf())) {
            return ResponseEntity.badRequest().body("Erro nos Dados do Body");
        }
        try {
            clientes.setSaldo(0.0);
            clientesRepository.save(clientes);
            return ResponseEntity.ok("Cliente Cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar cliente!");
        }
    }


    @PutMapping("/sacar/{id}/{quantidade}")
    public ResponseEntity<String> sacar(@PathVariable Long id, @PathVariable double quantidade) {
        Optional<Clientes> clientesOptional = clientesRepository.findById(id);

        if (clientesOptional.isPresent()) {
            Clientes clientes = clientesOptional.get();
            double saldoAtual = clientes.getSaldo();


                //Obtendo Id da empresa
                Long empresasId = clientes.getEmpresa();
                Optional<Empresas> empresasOptional = empresasRepository.findById(empresasId);

                //Atualiza saldo da empresa
                if (empresasOptional.isPresent()) {
                    Empresas empresas = empresasOptional.get();
                    double saldoEmpresa = empresas.getSaldo();
                    double taxa = quantidade * 0.0001;
                    saldoEmpresa -= quantidade + taxa;
                    empresas.setSaldo(saldoEmpresa);

                    if (saldoAtual >= quantidade) {
                        //Atualizando Saque do Cliente
                        saldoAtual -= quantidade;
                        clientes.setSaldo(saldoAtual);
                        clientesRepository.save(clientes);

                        try {
                            String emailCliente = clientes.getEmail();
                            String mensagemCliente = "Transação concluída (CLIENTE): Foi realizado um saque de " + quantidade + " na sua conta. - Java Banks Agradece.";
                            emailService.sendEmail(emailCliente, subject, mensagemCliente);
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Operação Realizada. Erro ao enviar Email para o cliente!");
                        }

                    //Atualiza taxas da Empresa
                    double taxasEmpresa = empresas.getTaxas();
                    taxasEmpresa += taxa;
                    empresas.setTaxas(taxasEmpresa);

                    try {
                        //Mensagem para empresa
                        String empresaEmail = empresas.getEmail();
                        String mensagemEmpresa = "Transação concluída (EMPRESA): Um saque de " + quantidade + " foi realizado por um cliente. - Java Banks Agradece.";
                        emailService.sendEmail(empresaEmail, subject, mensagemEmpresa);

                        empresasRepository.save(empresas);
                        return ResponseEntity.ok("Saque de " + quantidade + ". Novo Saldo: " + saldoAtual);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Operação Realizada. Erro ao enviar Email para a empresa!");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Saldo Insuficiente!");
                }
            } else {
                return ResponseEntity.badRequest().body("Empresa Não Encontrada");
            }
        } else {
            return ResponseEntity.badRequest().body("Cliente Não Encontrado");
        }
    }

    //Depositando com o ID do cliente e a quantidade a ser depositada
    @PutMapping("/depositar/{id}/{quantidade}")
    public ResponseEntity<String> depositar(@PathVariable Long id, @PathVariable double quantidade) {

        //Verificação da existência do cliente pelo ID
        Optional<Clientes> clientesOptional = clientesRepository.findById(id);

        //Definindo Saldo do cliente após verificação
        if (clientesOptional.isPresent()) {
            Clientes clientes = clientesOptional.get();

            // Verificação da existência da empresa pelo ID
            Long empresasId = clientes.getEmpresa();
            Optional<Empresas> empresasOptional = empresasRepository.findById(empresasId);

            if (empresasOptional.isPresent()) {
                Empresas empresas = empresasOptional.get();
                double saldoEmpresa = empresas.getSaldo();

                // Calcula a taxa
                double taxa = quantidade * 0.0001;

                // Deduz a taxa do saldo da empresa
                saldoEmpresa += quantidade - taxa;
                empresas.setSaldo(saldoEmpresa);

                // Atualiza as taxas da Empresa
                double taxasEmpresa = empresas.getTaxas();
                taxasEmpresa += taxa;
                empresas.setTaxas(taxasEmpresa);

                empresasRepository.save(empresas);

                //Atualiza saldo do Cliente
                double saldoCliente = clientes.getSaldo();
                saldoCliente += quantidade;
                clientes.setSaldo(saldoCliente);

                clientesRepository.save(clientes);

                try {
                    // Enviar um email de notificação para o cliente
                    String emailCliente = clientes.getEmail();
                    String mensagemCliente = "Transação concluída (CLIENTE): Foi realizado um depósito de " + quantidade + " na sua conta. - Java Banks Agradece.";
                    emailService.sendEmail(emailCliente, subject, mensagemCliente);
                } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Operação Realizada. Erro ao enviar Email para o Cliente");
            }
                try {
                // Enviar um email de notificação para a empresa
                String empresaEmail = empresas.getEmail();
                String mensagemEmpresa = "Transação concluída (EMPRESA): Um depósito de " + quantidade + " foi realizado por um cliente. - Java Banks Agradece.";
                emailService.sendEmail(empresaEmail, subject, mensagemEmpresa);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Operação Realizada. Erro ao enviar Email para a Empresa");
                }

                return ResponseEntity.ok("Depósito de " + quantidade + ". Saldo: " + saldoCliente);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("Erro ao identificar cliente");
        }
    }
}
