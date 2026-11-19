# 📚 01 - Fundamentos de Kotlin

Módulo focado em assimilar a sintaxe, recursos essenciais e o estilo idiomático da linguagem Kotlin para o ecossistema Back-End.

---

## 🛒 Exercício Prático: Checkout de E-Commerce

Neste exercício modelamos um fluxo de checkout de pedidos aplicando os principais pilares da Fase 1 da formação:
- **Imutabilidade:** Modelagem estrita de entidades com `val` e coleções `List<T>` *read-only*.
- **Null Safety:** Proteção em tempo de compilação contra NPEs com tipos anuláveis (`Endereco?`, `Cupom?`), *Safe Call* (`?.`) e operador Elvis (`?:`).
- **Controle de Fluxo com `when`:** Cálculo de frete baseado em *ranges* numéricos e avaliação de status de pedido sem necessidade de `else`.

### 📸 Telas de Execução

#### 1. Execução da Aplicação (`.\gradlew.bat :01-fundamentos:run`)
Demonstração do processamento comparando um pedido físico com cupom de desconto versus um produto digital isento de frete e sem cupom:

![Execução do Checkout](../assets/screenshots/execucao_checkout.png)

---

#### 2. Suíte de Testes Automatizados com JUnit 5 (`.\gradlew.bat test`)
Cobertura de 100% dos ramos de negócio e casos de borda com testes unitários expressivos:

![Testes Unitários](../assets/screenshots/testes_checkout.png)

---

### 💻 Estrutura dos Arquivos

- **[Modelos.kt](src/main/kotlin/br/com/treinamento/fundamentos/checkout/Modelos.kt):** Enums, Data Classes imutáveis e entidades do domínio.
- **[CheckoutService.kt](src/main/kotlin/br/com/treinamento/fundamentos/checkout/CheckoutService.kt):** Regras de negócio, cálculo de frete via `when` e descontos com operador Elvis.
- **[CheckoutServiceTest.kt](src/test/kotlin/br/com/treinamento/fundamentos/checkout/CheckoutServiceTest.kt):** Testes unitários com JUnit 5 cobrindo subtotal, frete, cupons e status.
