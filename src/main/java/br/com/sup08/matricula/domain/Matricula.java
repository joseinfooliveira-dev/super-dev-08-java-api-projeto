package br.com.sup08.matricula.domain;

import br.com.sup08.aluno.domain.Aluno;
import br.com.sup08.turma.domain.Turma;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tb_matricula", uniqueConstraints = {
        @UniqueConstraint(name = "uk_matricula_aluno_turma", columnNames = {"aluno_id", "turma_id"})
})
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Matricula() {}

    public Matricula(Aluno aluno, Turma turma) {
        this.aluno = aluno;
        this.turma = turma;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }
    public Instant getCreatedAt() { return createdAt; }
}