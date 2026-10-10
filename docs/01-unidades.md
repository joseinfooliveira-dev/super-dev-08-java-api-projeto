# Unidades

[◀ Índice](README.md)

## Regras de negócio

| ID | Regra |
| --- | --- |
| RG0100 | Não deve permitir duplicidade de sigla da unidade ao cadastrar. Comparação *case-insensitive* e com `trim` (`sp` == `SP ` == `SP`). |
| RG0101 | Não deve permitir editar a sigla da unidade. O campo é ignorado/rejeitado na edição. |
| RG0102 | Nome é obrigatório, de 3 a 120 caracteres; sigla é obrigatória, de 2 a 10 caracteres alfanuméricos, persistida em maiúsculas. |
| RG0103 | Não deve permitir duplicidade de nome da unidade (*case-insensitive*). |
| RG0104 | Unidade não pode ser excluída enquanto houver turmas vinculadas a ela. |

## Requisitos funcionais

| ID | Requisito |
| --- | --- |
| RF0100 | Deve permitir cadastrar unidade. |
| RF0101 | Deve permitir editar unidade (apenas o nome — ver RG0101). |
| RF0102 | Deve permitir listar as unidades, com paginação e ordenação. |
| RF0103 | Deve permitir obter a unidade filtrando por id. |
| RF0104 | Deve permitir listar as unidades filtrando por nome e/ou sigla (busca parcial, *case-insensitive*). |

## Relacionados

- [Turmas](04-turmas.md) — RG0402 (nome de turma único por unidade) e RG0409 (unidade imutável na turma).
- [Mapa de endpoints](07-endpoints.md#unidades)
