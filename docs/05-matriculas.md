# Matrículas (TurmasAlunos)

[◀ Índice](README.md)

## Regras de negócio

| ID | Regra |
| --- | --- |
| RG0500 | Não deve permitir matricular o mesmo aluno mais de uma vez na mesma turma. |
| RG0501 | Só deve permitir matrícula em turma com status `ATIVA` (ver [RG0406](04-turmas.md)). |
| RG0502 | Turma e aluno informados devem existir. |
| RG0503 | A matrícula só pode ser removida enquanto a turma não possuir nenhuma aula `FINALIZADA`. |
| RG0504 | Na matrícula em lote a operação é atômica: se qualquer aluno da lista violar uma regra, nenhuma matrícula é efetivada. |

## Requisitos funcionais

| ID | Requisito |
| --- | --- |
| RF0500 | Deve permitir matricular um aluno em uma turma. |
| RF0501 | Deve permitir matricular vários alunos em uma turma em uma única requisição. |
| RF0502 | Deve permitir remover a matrícula de um aluno de uma turma. |
| RF0503 | Deve permitir listar os alunos matriculados em uma turma, com paginação. |
| RF0504 | Deve permitir listar as turmas de um aluno (mesmo recurso de [RF0304](03-alunos.md)). |

## Relacionados

- [Alunos](03-alunos.md) — RG0306: aluno com matrícula não pode ser excluído.
- [Turmas](04-turmas.md) — RF0405: o total de matriculados compõe o progresso da turma.
- [Requisitos não funcionais](08-requisitos-nao-funcionais.md) — RNF07: constraint `UNIQUE (id_turma, id_aluno)` garante RG0500 no banco.
- [Mapa de endpoints](07-endpoints.md#matrículas)
