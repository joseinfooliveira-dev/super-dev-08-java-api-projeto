# Modelo de dados

[◀ Índice](README.md)

Diagrama ER, cardinalidades e restrições esperadas em [09-diagrama-banco.md](09-diagrama-banco.md).

## Tabelas

### Unidades
- id
- nome
- sigla

### Professores
- id
- nome
- status — `ProfessorStatus`

### Alunos
- id
- nome
- cpf
- data nascimento

### Turmas
- id
- id professor — FK `Professores`
- id unidade — FK `Unidades` *(ver ponto de atenção 1)*
- nome
- status — `TurmaStatus`
- carga horaria — em horas inteiras

### TurmasAlunos (NxN)
- id
- id turma — FK `Turmas`
- id aluno — FK `Alunos`

### Aulas
- id
- status — `AulaStatus`
- id turma — FK `Turmas`
- data hora inicio
- data hora fim
- motivo cancelamento — opcional *(ver ponto de atenção 4)*

## Enumerações

| Enum | Valores |
| --- | --- |
| `ProfessorStatus` | `ATIVO`, `INATIVO` |
| `TurmaStatus` | `ATIVA`, `INATIVA`, `CONCLUIDA` |
| `AulaStatus` | `PENDENTE`, `EM_ANDAMENTO`, `FINALIZADA`, `CANCELADA` |

## Pontos de atenção

Lacunas identificadas no modelo original e como foram tratadas nesta especificação:

1. **`Unidades` não possuía vínculo com nenhuma outra tabela.** Assumi `Turmas.id_unidade` (FK obrigatória) — é o
   que dá sentido às unidades e sustenta [RG0402, RG0409](04-turmas.md) e [RG0104](01-unidades.md). Se a intenção
   era vincular a unidade ao professor ou ao aluno, essas regras precisam ser reescritas.
2. **`Alunos` não possui `status`.** Sem ele não há como inativar aluno — daí [RG0306](03-alunos.md), que bloqueia
   a exclusão. Se a inativação for necessária, incluir a coluna `status` e uma regra análoga a
   [RG0200](02-professores.md).
3. **`Turmas.status` não tinha domínio definido.** Adotei `ATIVA`/`INATIVA`/`CONCLUIDA`; `ATIVA` é o valor exigido
   por [RG0200](02-professores.md).
4. **`Aulas` não tem campo de motivo de cancelamento.** [RG0610](06-aulas.md) pressupõe cancelamento em vez de
   exclusão; sugerido adicionar `motivo_cancelamento` para rastreabilidade.
5. **`Aulas` não controla presença de alunos.** Se for necessário registrar frequência, criar `AulasAlunos` (NxN)
   com presença, o que também altera [RG0503](05-matriculas.md).
6. **`Turmas.carga_horaria` não definia unidade de medida.** Assumi horas inteiras ([RG0401](04-turmas.md)).
