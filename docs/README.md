# Projeto SUP-08 — Documentação

API REST em Java (Spring Boot) para gestão de unidades, professores, alunos, turmas e aulas.

## Índice

| Arquivo | Conteúdo |
| --- | --- |
| [00-modelo-de-dados.md](00-modelo-de-dados.md) | Tabelas, enumerações e pontos de atenção do modelo |
| [01-unidades.md](01-unidades.md) | Unidades — RG0100+ / RF0100+ |
| [02-professores.md](02-professores.md) | Professores — RG0200+ / RF0200+ |
| [03-alunos.md](03-alunos.md) | Alunos — RG0300+ / RF0300+ |
| [04-turmas.md](04-turmas.md) | Turmas — RG0400+ / RF0400+ |
| [05-matriculas.md](05-matriculas.md) | Matrículas (TurmasAlunos) — RG0500+ / RF0500+ |
| [06-aulas.md](06-aulas.md) | Aulas — RG0600+ / RF0600+ |
| [07-endpoints.md](07-endpoints.md) | Mapa de endpoints por requisito funcional |
| [08-requisitos-nao-funcionais.md](08-requisitos-nao-funcionais.md) | Padrões técnicos da API (RNF01+) |
| [09-diagrama-banco.md](09-diagrama-banco.md) | Diagrama ER, cardinalidades e restrições esperadas |
| [10-diagramas-de-estado.md](10-diagramas-de-estado.md) | Ciclo de vida de Aula, Turma, Professor e Matrícula |
| [11-diagramas-de-fluxo.md](11-diagramas-de-fluxo.md) | Fluxo das operações com mais regras de negócio |
| [12-arquitetura.md](12-arquitetura.md) | Camadas, módulos e mapa de regras por módulo |

Os diagramas estão em [Mermaid](https://mermaid.js.org/), renderizado nativamente pelo GitHub e pelo VS Code
(extensão *Markdown Preview Mermaid Support*).

## Convenções

- **RG0X00** = regra de negócio; **RF0X00** = requisito funcional. O bloco `0X` identifica a entidade:
  `01` Unidades · `02` Professores · `03` Alunos · `04` Turmas · `05` Matrículas · `06` Aulas.
- Toda violação de regra de negócio retorna **409 Conflict** com o código da regra no corpo do erro
  (ver [RNF05](08-requisitos-nao-funcionais.md)). Violação de formato ou obrigatoriedade retorna **400 Bad Request**.
- Nenhuma entidade é removida fisicamente: usa-se inativação (Professor, Turma) ou cancelamento (Aula).
- Ao incluir uma nova regra, use o próximo número livre do bloco da entidade e não reaproveite códigos
  de regras removidas — os códigos são referenciados nas respostas de erro da API.
