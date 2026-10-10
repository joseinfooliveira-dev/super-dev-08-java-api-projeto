# Diagramas de estado

[◀ Índice](README.md)

Todas as transições abaixo são as únicas válidas: qualquer outra tentativa é rejeitada com `409`
e o código da regra ([RNF04, RNF05](08-requisitos-nao-funcionais.md)).

## Aula ([RG0605](06-aulas.md))

```mermaid
stateDiagram-v2
    [*] --> PENDENTE : agendar (RF0600)
    PENDENTE --> EM_ANDAMENTO : iniciar (RF0602)
    PENDENTE --> CANCELADA : cancelar (RF0604)
    EM_ANDAMENTO --> FINALIZADA : finalizar (RF0603)
    EM_ANDAMENTO --> CANCELADA : cancelar (RF0604)
    FINALIZADA --> [*]
    CANCELADA --> [*]

    note right of PENDENTE
        Único estado em que
        datas podem ser editadas
        (RG0607)
    end note
    note right of EM_ANDAMENTO
        No máximo uma por turma
        simultaneamente (RG0606)
    end note
    note right of FINALIZADA
        Soma das horas alimenta
        RG0407 e RG0609
    end note
```

Estados finais: `FINALIZADA` e `CANCELADA`. Não existe exclusão física de aula ([RG0610](06-aulas.md)).

## Turma ([RG0403](04-turmas.md), [RG0405](04-turmas.md), [RG0407](04-turmas.md))

```mermaid
stateDiagram-v2
    [*] --> ATIVA : cadastrar (RF0400)
    ATIVA --> INATIVA : inativar (RF0402)
    ATIVA --> CONCLUIDA : horas realizadas >= carga horária (RG0407)
    INATIVA --> [*]
    CONCLUIDA --> [*]

    note right of ATIVA
        Único estado que aceita
        novas aulas e matrículas
        (RG0406)
    end note
    note right of INATIVA
        Exige nenhuma aula EM_ANDAMENTO;
        cancela as PENDENTES em cascata
        (RG0405)
    end note
```

> **Em aberto:** a reativação de turma (`INATIVA` → `ATIVA`) não foi especificada. Se for necessária, criar
> RF0406/RG0410 no modelo da [reativação de professor](02-professores.md).

## Professor ([RG0200](02-professores.md), [RG0202](02-professores.md), [RG0206](02-professores.md))

```mermaid
stateDiagram-v2
    [*] --> ATIVO : cadastrar (RF0200)
    ATIVO --> INATIVO : inativar (RF0202) — exige zero turmas ATIVAS (RG0200)
    INATIVO --> ATIVO : reativar (RF0205) — sempre permitido (RG0206)

    note right of INATIVO
        Não pode ser vinculado
        a novas turmas (RG0204)
    end note
```

Inativar quem já está `INATIVO` e reativar quem já está `ATIVO` são operações idempotentes
([RG0205](02-professores.md)) — não são transições, retornam sucesso sem alterar o registro.

## Matrícula

Não possui coluna de status: existe ou não existe. A remoção é física e condicionada a
[RG0503](05-matriculas.md) (turma sem nenhuma aula `FINALIZADA`).

```mermaid
stateDiagram-v2
    [*] --> MATRICULADO : matricular (RF0500/RF0501) — turma ATIVA (RG0501)
    MATRICULADO --> [*] : remover (RF0502) — sem aula FINALIZADA (RG0503)
```
