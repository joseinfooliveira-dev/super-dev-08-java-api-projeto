# Alunos

[◀ Índice](README.md)

## Regras de negócio

| ID | Regra |
| --- | --- |
| RG0300 | Não deve permitir duplicidade de CPF ao cadastrar aluno. |
| RG0301 | O CPF deve ser válido (11 dígitos + dígitos verificadores), aceito com ou sem máscara na entrada e persistido sem máscara. |
| RG0302 | Não deve permitir editar o CPF do aluno. |
| RG0303 | A data de nascimento é obrigatória e não pode ser futura nem anterior a 120 anos da data atual. |
| RG0304 | O aluno deve ter no mínimo 16 anos completos na data do cadastro. |
| RG0305 | Nome é obrigatório, de 3 a 150 caracteres. |
| RG0306 | Aluno não pode ser excluído enquanto possuir matrícula em turma. |

## Requisitos funcionais

| ID | Requisito |
| --- | --- |
| RF0300 | Deve permitir cadastrar aluno. |
| RF0301 | Deve permitir editar aluno (nome e data de nascimento — ver RG0302). |
| RF0302 | Deve permitir listar os alunos filtrando por nome (parcial) e/ou CPF (exato), com paginação. |
| RF0303 | Deve permitir buscar aluno por id. |
| RF0304 | Deve permitir listar as turmas em que o aluno está matriculado. |

## Relacionados

- [Matrículas](05-matriculas.md) — RG0500 a RG0504.
- [Modelo de dados](00-modelo-de-dados.md) — ponto de atenção 2: `Alunos` não possui `status`, o que impede a inativação.
- [Mapa de endpoints](07-endpoints.md#alunos)
