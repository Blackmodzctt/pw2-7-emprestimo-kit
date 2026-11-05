package kits_maker.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "emprestimo_kit")
public class EmprestimoKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_kit", nullable = false)
    private String codigoKit;

    @Column(name = "nome_aluno", nullable = false)
    private String nomeAluno;

    @Column(name = "horario_retirada", nullable = false)
    private LocalTime horarioRetirada;

    @Column(name = "horario_limite_devolucao", nullable = false)
    private LocalTime horarioLimiteDevolucao;

    @Column(name = "horario_devolucao_real")
    private LocalTime horarioDevolucaoReal;

    @Column(name = "status", nullable = false)
    private String status;

    public EmprestimoKit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalTime getHorarioRetirada() {
        return horarioRetirada;
    }

    public void setHorarioRetirada(LocalTime horarioRetirada) {
        this.horarioRetirada = horarioRetirada;
    }

    public LocalTime getHorarioLimiteDevolucao() {
        return horarioLimiteDevolucao;
    }

    public void setHorarioLimiteDevolucao(LocalTime horarioLimiteDevolucao) {
        this.horarioLimiteDevolucao = horarioLimiteDevolucao;
    }

    public LocalTime getHorarioDevolucaoReal() {
        return horarioDevolucaoReal;
    }

    public void setHorarioDevolucaoReal(LocalTime horarioDevolucaoReal) {
        this.horarioDevolucaoReal = horarioDevolucaoReal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

