# Professores

[◀ Índice](README.md)

## Regras de negócio

| ID | Regra |
| --- | --- |
| RG0200 | O professor pode ser inativado somente quando nenhuma turma com status `ATIVA` estiver atrelada a ele. |
| RG0201 | Nome é obrigatório, de 3 a 150 caracteres. |
| RG0202 | Todo professor é criado com status `ATIVO`. O status não é aceito no payload de cadastro. |
| RG0203 | O status não pode ser alterado pelo endpoint de edição — somente pelos endpoints de inativar/reativar. |
| RG0204 | Não deve permitir vincular um professor `INATIVO` a uma turma (no cadastro da turma ou na troca de professor). |
| RG0205 | Inativar um professor já `INATIVO` (ou reativar um já `ATIVO`) é idempotente: retorna sucesso sem alterar o registro. |
| RG0206 | A reativação de professor é sempre permitida, sem validações adicionais. |

## Requisitos funcionais

| ID | Requisito |
| --- | --- |
| RF0200 | Deve permitir cadastrar o professor. |
| RF0201 | Deve permitir editar o professor. |
| RF0202 | Deve permitir inativar professor. |
| RF0203 | Deve permitir listar os professores filtrando por nome (busca parcial) e por status, com paginação. |
| RF0204 | Deve permitir buscar professor por id. |
| RF0205 | Deve permitir reativar professor. |
| RF0206 | Deve permitir listar as turmas de um professor. |

## Relacionados

- [Turmas](04-turmas.md) — RG0400 (professor ativo no cadastro) e RG0404 (troca de professor).
- [Aulas](06-aulas.md) — RG0603 (sem sobreposição de horário para o mesmo professor) e RF0607 (agenda do professor).
- [Mapa de endpoints](07-endpoints.md#professores)
