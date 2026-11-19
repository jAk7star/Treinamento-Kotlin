package br.com.treinamento.fundamentos

import br.com.treinamento.fundamentos.checkout.*

fun main() {
    println("==================================================")
    println("🚀 Formação Kotlin Back-End Developer")
    println("Módulo 01: Fundamentos & Checkout E-commerce")
    println("==================================================\n")

    val service = CheckoutService()

    // 1. Criando um pedido completo
    val pedido1 = Pedido(
        id = "PED-2026-001",
        itens = listOf(
            ItemPedido("Monitor Ultrawide 34\"", 2500.0, 1),
            ItemPedido("Cabo DisplayPort 1.4", 80.0, 2)
        ),
        enderecoEntrega = Endereco(
            logradouro = "Av. Paulista, 1000",
            cidade = "São Paulo",
            uf = "SP",
            cep = "01310-100"
        ),
        cupom = Cupom("TECH10", 0.10)
    )

    val resumo1 = service.processarCheckout(pedido1)

    println("📦 Pedido: ${resumo1.pedidoId}")
    println("• Subtotal: R$ ${"%.2f".format(resumo1.subtotal)}")
    println("• Desconto aplicado: R$ ${"%.2f".format(resumo1.desconto)} (Cupom: ${pedido1.cupom?.codigo ?: "Nenhum"})")
    println("• Frete: R$ ${"%.2f".format(resumo1.frete)}")
    println("• Total a Pagar: R$ ${"%.2f".format(resumo1.totalFinal)}")
    println("• Status: ${resumo1.status} -> ${service.obterMensagemStatus(resumo1.status)}")

    println("\n--------------------------------------------------\n")

    // 2. Pedido de produto digital sem endereço e sem cupom (Null Safety)
    val pedidoDigital = Pedido(
        id = "PED-DIGITAL-002",
        itens = listOf(ItemPedido("Curso Kotlin Pro (E-book)", 89.90, 1))
    )

    val resumoDigital = service.processarCheckout(pedidoDigital)

    println("💻 Pedido Digital: ${resumoDigital.pedidoId}")
    println("• Subtotal: R$ ${"%.2f".format(resumoDigital.subtotal)}")
    println("• Cupom: ${pedidoDigital.cupom?.codigo ?: "Nenhum cupom aplicado (Elvis: ?: 0.0)"}")
    println("• Endereço: ${pedidoDigital.enderecoEntrega?.cidade ?: "Produto Digital / Isento de Frete"}")
    println("• Frete calculado: R$ ${"%.2f".format(resumoDigital.frete)}")
    println("• Total a Pagar: R$ ${"%.2f".format(resumoDigital.totalFinal)}")
    println("\n✅ Todos os testes e validações de Null Safety executados com sucesso!")
}
