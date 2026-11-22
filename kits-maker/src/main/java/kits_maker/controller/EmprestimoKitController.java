package kits_maker.controller;

import kits_maker.entity.EmprestimoKit;
import kits_maker.repository.EmprestimoKitRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
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

    @Setter
    @Getter
    public static class EmprestimoRequest {

        @NotBlank
        private String codigoKit;

        @NotBlank
        private String nomeAluno;

    }

    // GET - Listar todos os empréstimos
    @GetMapping
    public ResponseEntity<List<EmprestimoKit>> listar() {

        List<EmprestimoKit> lista = repository.findAll();

        return ResponseEntity.ok(lista);
    }

    // GET - Buscar empréstimo por ID
    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoKit> buscarPorId(@PathVariable Long id) {

        EmprestimoKit emprestimo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        return ResponseEntity.ok(emprestimo);
    }

    // POST - Cadastrar empréstimo
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

    // PUT - Atualizar empréstimo
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

    // DELETE - Excluir empréstimo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Empréstimo não encontrado");
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}

