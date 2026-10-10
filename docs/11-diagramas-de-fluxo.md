# Diagramas de fluxo das operações

[◀ Índice](README.md)

Fluxos das operações que concentram regras de negócio. A ordem de validação importa: valida-se primeiro o que
custa menos (formato, existência) e depois o que exige consulta ([RNF03](08-requisitos-nao-funcionais.md)).

## Agendar aula ([RF0600](06-aulas.md))

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    participant AC as AulaController
    participant AS as AulaService
    participant TR as TurmaRepository
    participant AR as AulaRepository

    C->>AC: POST /api/v1/aulas
    AC->>AC: Bean Validation do payload (RNF03)
    AC->>AS: agendar(AulaRequest)
    AS->>AS: valida fim > inicio (RG0600) e duração <= 24h (RG0611)
    AS->>AS: valida inicio no futuro (RG0608)
    AS->>TR: findById(idTurma)
    TR-->>AS: Turma
    alt turma não existe
        AS-->>C: 404 Not Found
    else turma não está ATIVA
        AS-->>C: 409 RG0601
    end
    AS->>AR: existeSobreposicaoNaTurma(idTurma, inicio, fim)
    alt há sobreposição na turma
        AS-->>C: 409 RG0602
    end
    AS->>AR: existeSobreposicaoDoProfessor(idProfessor, inicio, fim)
    alt há sobreposição do professor
        AS-->>C: 409 RG0603
    end
    AS->>AR: save(aula com status PENDENTE) (RG0604)
    AR-->>AS: Aula
    AS-->>AC: AulaResponse
    AC-->>C: 201 Created + Location (RNF04)
```

## Finalizar aula e conclusão automática da turma ([RF0603](06-aulas.md), [RG0407](04-turmas.md))

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    participant AS as AulaService
    participant AR as AulaRepository
    participant TS as TurmaService

    C->>AS: PATCH /api/v1/aulas/{id}/finalizar
    AS->>AR: findById(id)
    AR-->>AS: Aula
    alt status != EM_ANDAMENTO
        AS-->>C: 409 RG0605 (transição inválida)
    end
    AS->>AR: somaHorasFinalizadas(idTurma)
    AR-->>AS: horasRealizadas
    alt horasRealizadas + duração > cargaHoraria
        AS-->>C: 409 RG0609
    end
    AS->>AR: atualiza status = FINALIZADA
    AS->>TS: avaliarConclusao(idTurma)
    alt horasRealizadas >= cargaHoraria
        TS->>TS: status da turma = CONCLUIDA (RG0407)
    end
    AS-->>C: 200 OK
```

A mesma transação cobre a aula e a turma ([RNF08](08-requisitos-nao-funcionais.md)): se a conclusão falhar, a
finalização da aula não é efetivada.

## Inativar professor ([RF0202](02-professores.md), [RG0200](02-professores.md))

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    participant PS as ProfessorService
    participant PR as ProfessorRepository
    participant TR as TurmaRepository

    C->>PS: PATCH /api/v1/professores/{id}/inativar
    PS->>PR: findById(id)
    PR-->>PS: Professor
    alt professor não existe
        PS-->>C: 404 Not Found
    else já está INATIVO
        PS-->>C: 200 OK (idempotente, RG0205)
    end
    PS->>TR: countByProfessorAndStatus(id, ATIVA)
    TR-->>PS: total
    alt total > 0
        PS-->>C: 409 RG0200
    end
    PS->>PR: atualiza status = INATIVO
    PS-->>C: 200 OK
```

## Inativar turma com cascata nas aulas ([RF0402](04-turmas.md), [RG0405](04-turmas.md))

```mermaid
flowchart TD
    A[PATCH /turmas/id/inativar] --> B{Turma existe?}
    B -- não --> B1[404 Not Found]
    B -- sim --> C{Existe aula EM_ANDAMENTO?}
    C -- sim --> C1[409 RG0405]
    C -- não --> D[status da turma = INATIVA]
    D --> E[Cancela todas as aulas PENDENTE da turma]
    E --> F[200 OK]
```

## Matricular alunos em lote ([RF0501](05-matriculas.md), [RG0504](05-matriculas.md))

```mermaid
flowchart TD
    A[POST /turmas/id/alunos] --> B{Turma existe?}
    B -- não --> B1[404 Not Found]
    B -- sim --> C{Turma está ATIVA?}
    C -- não --> C1[409 RG0501]
    C -- sim --> D[Para cada aluno da lista]
    D --> E{Aluno existe?}
    E -- não --> E1[404 - desfaz tudo RG0504]
    E -- sim --> F{Já matriculado na turma?}
    F -- sim --> F1[409 RG0500 - desfaz tudo RG0504]
    F -- não --> G[Inclui matrícula]
    G --> H{Resta aluno na lista?}
    H -- sim --> D
    H -- não --> I[Commit único - 201 Created]
```

## Cadastrar unidade ([RF0100](01-unidades.md)) e editar ([RF0101](01-unidades.md))

```mermaid
flowchart TD
    A[POST /unidades] --> B[Normaliza: trim no nome, sigla em maiúsculas]
    B --> C{Nome e sigla no formato? RG0102}
    C -- não --> C1[400 Bad Request]
    C -- sim --> D{Sigla já existe? RG0100}
    D -- sim --> D1[409 RG0100]
    D -- não --> E{Nome já existe? RG0103}
    E -- sim --> E1[409 RG0103]
    E -- não --> F[201 Created]

    G[PUT /unidades/id] --> H{Payload traz sigla diferente da atual?}
    H -- sim --> H1[409 RG0101]
    H -- não --> I[Atualiza somente o nome, revalidando RG0103]
    I --> J[200 OK]
```
