# Arquitetura

[◀ Índice](README.md)

## Camadas ([RNF02](08-requisitos-nao-funcionais.md))

```mermaid
flowchart TD
    CLIENTE[Cliente HTTP / Swagger UI]

    subgraph API["Aplicação Spring Boot"]
        direction TB
        CTRL["controller<br/>mapeia rotas, Bean Validation do payload (RNF03)<br/>traduz para DTO, define o status HTTP (RNF04)"]
        SVC["service<br/>regras de negócio RG, transações (RNF08)"]
        REPO["repository<br/>Spring Data JPA, consultas"]
        ERR["common.ErrorHandler<br/>@RestControllerAdvice, ProblemDetail (RNF05)"]
    end

    DB[("PostgreSQL<br/>constraints de unicidade e CHECK (RNF07)")]

    CLIENTE -->|JSON| CTRL
    CTRL --> SVC
    SVC --> REPO
    REPO --> DB
    SVC -.->|exceções de regra| ERR
    CTRL -.->|erros de validação| ERR
    ERR -->|"400 / 404 / 409 + codigo da regra"| CLIENTE

    classDef layer fill:#eef2ff,stroke:#4f46e5,color:#1e1b4b
    classDef edge fill:#f1f5f9,stroke:#64748b,color:#0f172a
    class CTRL,SVC,REPO layer
    class ERR,DB,CLIENTE edge
```

Regras do desenho:

- O `controller` nunca chama `repository`, e o `service` nunca conhece tipos de HTTP.
- Entidades JPA não cruzam a fronteira do `controller`: entrada e saída sempre por DTOs
  ([RNF02](08-requisitos-nao-funcionais.md)).
- Validação de formato fica nas anotações do DTO; validação de regra de negócio fica no `service`
  ([RNF03](08-requisitos-nao-funcionais.md)). O banco repete as unicidades como rede de segurança
  ([RNF07](08-requisitos-nao-funcionais.md)).

## Módulos e dependências

```mermaid
flowchart LR
    UNI[unidade]
    PROF[professor]
    ALU[aluno]
    TUR[turma]
    MAT[matricula]
    AUL[aula]
    COM[common]

    TUR --> UNI
    TUR --> PROF
    MAT --> TUR
    MAT --> ALU
    AUL --> TUR
    PROF -.->|"consulta turmas ATIVAS<br/>(RG0200)"| TUR
    AUL -.->|"conclusão automática<br/>(RG0407)"| TUR

    UNI --> COM
    PROF --> COM
    ALU --> COM
    TUR --> COM
    MAT --> COM
    AUL --> COM
```

As duas setas tracejadas são as únicas dependências de volta: `professor` precisa consultar turmas para
[RG0200](02-professores.md) e `aula` precisa atualizar a turma para [RG0407](04-turmas.md). Ambas passam pelo
*service* da turma — nunca pelo repositório dele — para não duplicar regra.

Estrutura de pacotes correspondente em
[08-requisitos-nao-funcionais.md](08-requisitos-nao-funcionais.md#estrutura-de-pacotes-sugerida-rnf02).

## Mapa de regras por módulo

```mermaid
flowchart TB
    subgraph U["unidade — RF0100 a RF0104"]
        U1["RG0100 sigla única"]
        U2["RG0101 sigla imutável"]
        U3["RG0102 formato"]
        U4["RG0103 nome único"]
        U5["RG0104 exclusão bloqueada"]
    end
    subgraph P["professor — RF0200 a RF0206"]
        P1["RG0200 inativação sem turma ATIVA"]
        P2["RG0204 inativo não recebe turma"]
        P3["RG0202/0203/0205/0206 ciclo de status"]
    end
    subgraph A["aluno — RF0300 a RF0304"]
        A1["RG0300/0301/0302 CPF"]
        A2["RG0303/0304 nascimento e idade"]
    end
    subgraph T["turma — RF0400 a RF0405"]
        T1["RG0400/0404 professor ATIVO"]
        T2["RG0401/0408 carga horária"]
        T3["RG0405/0406 inativação e bloqueios"]
        T4["RG0407 conclusão automática"]
    end
    subgraph M["matrícula — RF0500 a RF0504"]
        M1["RG0500 sem duplicidade"]
        M2["RG0501 turma ATIVA"]
        M3["RG0503/0504 remoção e lote"]
    end
    subgraph AU["aula — RF0600 a RF0607"]
        AU1["RG0602/0603 sem sobreposição"]
        AU2["RG0605/0606 máquina de estados"]
        AU3["RG0600/0608/0611 período"]
        AU4["RG0609 limite da carga horária"]
    end

    P1 --> T3
    T1 --> P2
    T4 --> AU4
    M2 --> T3
    AU2 --> T4
```
