# Requisitos não funcionais / padrões da API

[◀ Índice](README.md)

| ID | Requisito |
| --- | --- |
| RNF01 | Java 21 + Spring Boot 3.x (Web, Data JPA, Validation), PostgreSQL e Flyway para migrações. |
| RNF02 | Arquitetura em camadas: `controller` (HTTP) → `service` (regras de negócio) → `repository` (persistência). Entidades JPA nunca são expostas: entrada e saída sempre por DTOs. |
| RNF03 | Validação de formato e obrigatoriedade via Bean Validation (`@NotBlank`, `@Size`, `@Positive`, `@Past` e validador customizado de CPF). As regras de negócio (RG) são validadas no service. |
| RNF04 | Códigos HTTP: `201` + header `Location` no cadastro; `200` em consulta e edição; `204` em remoção; `400` em erro de validação; `404` em recurso inexistente; `409` em violação de regra de negócio. |
| RNF05 | Resposta de erro padronizada (RFC 7807 / `ProblemDetail`) contendo `codigo` com o identificador da regra violada (ex.: `RG0100`), `mensagem` e, em erros de validação, a lista de campos. |
| RNF06 | Toda listagem é paginada (`page`, `size`, `sort`) e retorna conteúdo + metadados de paginação. |
| RNF07 | Unicidade garantida também por constraint no banco (`UNIQUE` em `unidades.sigla`, `unidades.nome`, `alunos.cpf` e `turmas_alunos (id_turma, id_aluno)`), além da validação em código. |
| RNF08 | Métodos de service que alteram estado são transacionais (`@Transactional`); operações em lote são atômicas (ver [RG0504](05-matriculas.md)). |
| RNF09 | Campos de auditoria `created_at` e `updated_at` em todas as tabelas. |
| RNF10 | Datas e horas persistidas em UTC; a API recebe e devolve no formato ISO-8601 com offset. |
| RNF11 | Documentação OpenAPI/Swagger gerada via springdoc. |
| RNF12 | Cobertura de testes: um teste unitário de service por RG e um teste de integração por RF. |

## Formato de erro (RNF05)

```json
{
  "type": "https://api.sup08.local/erros/regra-de-negocio",
  "title": "Violação de regra de negócio",
  "status": 409,
  "codigo": "RG0100",
  "mensagem": "Já existe uma unidade cadastrada com a sigla SP.",
  "campos": []
}
```

## Estrutura de pacotes sugerida (RNF02)

```
br.com.sup08
├── unidade      (controller, service, repository, domain, dto)
├── professor
├── aluno
├── turma
├── matricula
├── aula
└── common       (tratamento de erros, paginação, validadores)
```
