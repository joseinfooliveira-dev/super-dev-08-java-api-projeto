# Mapa de endpoints

[◀ Índice](README.md)

Prefixo comum: `/api/v1`. Códigos de resposta e formato de erro em
[08-requisitos-nao-funcionais.md](08-requisitos-nao-funcionais.md) (RNF04 e RNF05).

## Unidades

| Método | Rota | RF |
| --- | --- | --- |
| `POST` | `/api/v1/unidades` | [RF0100](01-unidades.md) |
| `PUT` | `/api/v1/unidades/{id}` | [RF0101](01-unidades.md) |
| `GET` | `/api/v1/unidades?nome=&sigla=` | [RF0102, RF0104](01-unidades.md) |
| `GET` | `/api/v1/unidades/{id}` | [RF0103](01-unidades.md) |

## Professores

| Método | Rota | RF |
| --- | --- | --- |
| `POST` | `/api/v1/professores` | [RF0200](02-professores.md) |
| `PUT` | `/api/v1/professores/{id}` | [RF0201](02-professores.md) |
| `PATCH` | `/api/v1/professores/{id}/inativar` | [RF0202](02-professores.md) |
| `PATCH` | `/api/v1/professores/{id}/reativar` | [RF0205](02-professores.md) |
| `GET` | `/api/v1/professores?nome=&status=` | [RF0203](02-professores.md) |
| `GET` | `/api/v1/professores/{id}` | [RF0204](02-professores.md) |
| `GET` | `/api/v1/professores/{id}/turmas` | [RF0206](02-professores.md) |
| `GET` | `/api/v1/professores/{id}/agenda?inicio=&fim=` | [RF0607](06-aulas.md) |

## Alunos

| Método | Rota | RF |
| --- | --- | --- |
| `POST` | `/api/v1/alunos` | [RF0300](03-alunos.md) |
| `PUT` | `/api/v1/alunos/{id}` | [RF0301](03-alunos.md) |
| `GET` | `/api/v1/alunos?nome=&cpf=` | [RF0302](03-alunos.md) |
| `GET` | `/api/v1/alunos/{id}` | [RF0303](03-alunos.md) |
| `GET` | `/api/v1/alunos/{id}/turmas` | [RF0304](03-alunos.md), [RF0504](05-matriculas.md) |

## Turmas

| Método | Rota | RF |
| --- | --- | --- |
| `POST` | `/api/v1/turmas` | [RF0400](04-turmas.md) |
| `PUT` | `/api/v1/turmas/{id}` | [RF0401](04-turmas.md) |
| `PATCH` | `/api/v1/turmas/{id}/inativar` | [RF0402](04-turmas.md) |
| `GET` | `/api/v1/turmas?nome=&professorId=&unidadeId=&status=` | [RF0403](04-turmas.md) |
| `GET` | `/api/v1/turmas/{id}` | [RF0404](04-turmas.md) |
| `GET` | `/api/v1/turmas/{id}/progresso` | [RF0405](04-turmas.md) |

## Matrículas

| Método | Rota | RF |
| --- | --- | --- |
| `POST` | `/api/v1/turmas/{id}/alunos` | [RF0500, RF0501](05-matriculas.md) |
| `DELETE` | `/api/v1/turmas/{id}/alunos/{alunoId}` | [RF0502](05-matriculas.md) |
| `GET` | `/api/v1/turmas/{id}/alunos` | [RF0503](05-matriculas.md) |

## Aulas

| Método | Rota | RF |
| --- | --- | --- |
| `POST` | `/api/v1/aulas` | [RF0600](06-aulas.md) |
| `PUT` | `/api/v1/aulas/{id}` | [RF0601](06-aulas.md) |
| `PATCH` | `/api/v1/aulas/{id}/iniciar` | [RF0602](06-aulas.md) |
| `PATCH` | `/api/v1/aulas/{id}/finalizar` | [RF0603](06-aulas.md) |
| `PATCH` | `/api/v1/aulas/{id}/cancelar` | [RF0604](06-aulas.md) |
| `GET` | `/api/v1/aulas?turmaId=&professorId=&status=&inicio=&fim=` | [RF0605](06-aulas.md) |
| `GET` | `/api/v1/aulas/{id}` | [RF0606](06-aulas.md) |
