package kits_maker.controller;

import kits_maker.entity.EmprestimoKit;
import kits_maker.repository.EmprestimoKitRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/kits-maker")
public class EmprestimoKitController {

    private final EmprestimoKitRepository repository;

    public EmprestimoKitController(EmprestimoKitRepository repository) {
        this.repository = repository;
    }

    public static class EmprestimoRequest {

        @NotBlank
        private String codigoKit;

        @NotBlank
        private String nomeAluno;

        public String getCodigoKit() {
            return codigoKit;
        }

        public void setCodigoKit(String codigoKit) {
            this.codigoKit = codigoKit;
        }

        public String getNomeAluno() {
            return nomeAluno;
        }

        public void setNomeAluno(String nomeAluno) {
            this.nomeAluno = nomeAluno;
        }
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoKit>> listar() {

        return ResponseEntity.ok(repository.findAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoKit> buscarPorId(@PathVariable Long id) {

        EmprestimoKit emprestimo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        return ResponseEntity.ok(emprestimo);

    }

    @PostMapping
    public ResponseEntity<EmprestimoKit> cadastrar(
            @Valid @RequestBody EmprestimoRequest dados) {

        EmprestimoKit emprestimo = new EmprestimoKit();

        LocalTime agora = LocalTime.now().withNano(0);

        emprestimo.setCodigoKit(dados.getCodigoKit());
        emprestimo.setNomeAluno(dados.getNomeAluno());
        emprestimo.setHorarioRetirada(agora);
        emprestimo.setHorarioLimiteDevolucao(agora.plusHours(2));
        emprestimo.setStatus("EM_USO");

        EmprestimoKit salvo = repository.save(emprestimo);

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);

    }

    @PutMapping("/{id}")
    public ResponseEntity<EmprestimoKit> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmprestimoRequest dados) {

        EmprestimoKit emprestimo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        emprestimo.setCodigoKit(dados.getCodigoKit());
        emprestimo.setNomeAluno(dados.getNomeAluno());

        EmprestimoKit atualizado = repository.save(emprestimo);

        return ResponseEntity.ok(atualizado);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Empréstimo não encontrado");
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<EmprestimoKit> devolver(@PathVariable Long id) {

        EmprestimoKit emprestimo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        if (!emprestimo.getStatus().equals("EM_USO")) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este kit já foi devolvido anteriormente.");
        }

        LocalTime agora = LocalTime.now().withNano(0);

        emprestimo.setHorarioDevolucaoReal(agora);

        if (agora.isAfter(emprestimo.getHorarioLimiteDevolucao())) {
            emprestimo.setStatus("DEVOLVIDO_COM_ATRASO");
        } else {
            emprestimo.setStatus("DEVOLVIDO_NO_PRAZO");
        }

        EmprestimoKit atualizado = repository.save(emprestimo);

        return ResponseEntity.ok(atualizado);

    }

}

