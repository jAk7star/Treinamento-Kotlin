# 🗺️ Roadmap de Estudos - Kotlin Back-End Developer

Acompanhamento de progresso nos tópicos e projetos da formação. Marque `[x]` conforme for concluindo cada item.

---

## 📌 Fase 1: Fundamentos & Mentalidade Kotlin
- [x] **Variáveis e Tipagem:** Diferenças e boas práticas entre `val` (imutável) e `var` (mutável).
- [x] **Null Safety:** Tipos anuláveis (`String?`), safe-call (`?.`), Elvis operator (`?:`), smart cast e por que evitar `!!`.
- [x] **Controle de Fluxo Expressivo:**
  - [x] `when` utilizado como expressão (retorno de valores).
  - [x] `if` / `else` como expressão.
  - [x] Ranges e loops (`in`, `step`, `downTo`).
- [x] **Funções:** Parâmetros nomeados, valores padrão (default arguments) e single-expression functions.

---

## 📌 Fase 2: POO Idiomática & Recursos Avançados
- [ ] **Classes & Construtores:** Construtores primários e secundários, `init block`.
- [ ] **Data Classes:** Geração automática de `equals`, `hashCode`, `toString`, `copy` e *destructuring declarations*.
- [ ] **Polimorfismo Moderno:** Uso de `sealed class` e `sealed interface` para modelagem de estados de domínio.
- [ ] **Objects & Companions:** `object` (Singleton) e `companion object` (substitutos para membros estáticos).
- [ ] **Extension Functions & Properties:** Criação de utilitários idiomáticos sem poluir as classes originais.
- [ ] **Scope Functions:** Domínio prático de `let`, `apply`, `also`, `run` e `with`.

---

## 📌 Fase 3: Paradigma Funcional & Manipulação de Coleções
- [ ] **High-Order Functions & Lambdas:** Passagem e retorno de funções, trailing lambdas.
- [ ] **Operações de Coleção:** `map`, `filter`, `reduce`, `fold`, `groupBy`, `any`, `all`, `associateBy`.
- [ ] **Sequences:** Processamento preguiçoso (*lazy evaluation*) para grandes volumes de dados.
- [ ] **Tratamento Idiomático de Erros:** Modelagem com `Result<T>` e Railway-Oriented Programming vs Exceções convencionais.

---

## 📌 Fase 4: Back-end com Spring Boot & Kotlin
- [ ] **Setup de Projeto Spring Boot com Gradle (Kotlin DSL):** Configuração de dependências e plugins (`kotlin-spring`, `kotlin-jpa`).
- [ ] **Arquitetura em Camadas:**
  - [ ] **Controllers:** Endpoints REST, mapeamento de requisições e respostas HTTP.
  - [ ] **DTOs & Validação:** Data classes para Request/Response com Bean Validation (`jakarta.validation`).
  - [ ] **Services & Injeção de Dependências:** Injeção via construtor idiomática em Kotlin.
  - [ ] **Persistência com Spring Data JPA:** Entidades com `allopen`/`noarg`, repositórios e queries derivadas.
- [ ] **Tratamento Global de Exceções:** `@RestControllerAdvice` e Problem Details (RFC 7807).
- [ ] **Documentação de API:** Configuração de Swagger / OpenAPI com SpringDoc.

---

## 📌 Fase 5: Testes & Qualidade de Software
- [ ] **Testes Unitários:** JUnit 5 com assertions expressivas.
- [ ] **Mocking com MockK:** `every`, `verify`, `slot` e `coEvery` (para corrotinas se aplicável).
- [ ] **Testes de Integração:** `@SpringBootTest` e `@AutoConfigureMockMvc`.

---

## 🚀 Desafios & Projetos Práticos
- [ ] **Desafio de Código 01:** [A definir / Nome do Desafio]
- [ ] **Desafio de Código 02:** [A definir / Nome do Desafio]
- [ ] **Projeto Final:** API RESTful Completa com Kotlin + Spring Boot
