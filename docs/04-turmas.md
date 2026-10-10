# Turmas

[◀ Índice](README.md)

## Regras de negócio

| ID | Regra |
| --- | --- |
| RG0400 | O professor informado deve existir e estar com status `ATIVO` no momento do cadastro (ver [RG0204](02-professores.md)). |
| RG0401 | A carga horária é obrigatória, expressa em horas inteiras, maior que 0 e menor ou igual a 9999. |
| RG0402 | Não deve permitir duplicidade de nome de turma dentro da mesma unidade (*case-insensitive*). |
| RG0403 | Toda turma é criada com status `ATIVA`. O status não é aceito no payload de cadastro. |
| RG0404 | A troca de professor só é permitida se a turma não estiver `CONCLUIDA` e o novo professor estiver `ATIVO`. |
| RG0405 | A turma só pode ser inativada se não houver aula com status `EM_ANDAMENTO`. Ao inativar, todas as aulas `PENDENTE` da turma são canceladas em cascata. |
| RG0406 | Turma com status `INATIVA` ou `CONCLUIDA` não aceita novas aulas nem novas matrículas. |
| RG0407 | A turma passa a `CONCLUIDA` quando a soma da duração das aulas `FINALIZADA` atinge ou supera a carga horária cadastrada. |
| RG0408 | A carga horária não pode ser reduzida para valor inferior ao total de horas já realizadas (aulas `FINALIZADA`). |
| RG0409 | A unidade informada deve existir e não pode ser alterada após o cadastro da turma. |

## Requisitos funcionais

| ID | Requisito |
| --- | --- |
| RF0400 | Deve permitir cadastrar turma. |
| RF0401 | Deve permitir editar turma (nome, carga horária e professor). |
| RF0402 | Deve permitir inativar turma. |
| RF0403 | Deve permitir listar as turmas filtrando por nome, professor, unidade e status, com paginação. |
| RF0404 | Deve permitir buscar turma por id. |
| RF0405 | Deve permitir obter o progresso da turma: carga horária contratada, horas realizadas, horas restantes e total de alunos matriculados. |

## Relacionados

- [Professores](02-professores.md) — RG0200: a turma `ATIVA` é o que bloqueia a inativação do professor.
- [Unidades](01-unidades.md) — RG0104: unidade com turmas vinculadas não pode ser excluída.
- [Matrículas](05-matriculas.md) — RG0501: matrícula apenas em turma `ATIVA`.
- [Aulas](06-aulas.md) — RG0601 e RG0609.
- [Mapa de endpoints](07-endpoints.md#turmas)
