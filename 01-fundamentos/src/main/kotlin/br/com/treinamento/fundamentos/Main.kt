package br.com.treinamento.fundamentos

import br.com.treinamento.fundamentos.checkout.*
import br.com.treinamento.fundamentos.pagamento.*
import java.time.LocalDate

fun main() {
    println("==================================================")
    println("🚀 Formação Kotlin Back-End Developer")
    println("Demonstração Integrada: Fase 1 & Fase 2")
    println("==================================================\n")

    // ==================================================
    // 🛒 FASE 1: Checkout E-Commerce (Fundamentos & Null Safety)
    // ==================================================
    println("📌 [FASE 1] Executando Checkout de E-Commerce...")
    val checkoutService = CheckoutService()

    val pedido = Pedido(
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

    val resumo = checkoutService.processarCheckout(pedido)

    println("📦 Pedido: ${resumo.pedidoId}")
    println("• Subtotal: ${resumo.subtotal.emMoedaBRL}")
    println("• Desconto: ${resumo.desconto.emMoedaBRL} (Cupom: ${pedido.cupom?.codigo ?: "Nenhum"})")
    println("• Frete: ${resumo.frete.emMoedaBRL}")
    println("• Total a Pagar: ${resumo.totalFinal.emMoedaBRL}")

    println("\n--------------------------------------------------\n")

    // ==================================================
    // 💳 FASE 2: Gateway de Pagamentos (POO Idiomática & Avançada)
    // ==================================================
    println("📌 [FASE 2] Processamento de Pagamento no Gateway...")
    val pagamentoService = PagamentoService()

    // 1. Pagamento via Cartão de Crédito em 6x
    val cartao = MetodoPagamento.CartaoCredito.deDados(
        valor = resumo.totalFinal,
        numero = "4111 2222 3333 4567",
        titular = "Jaques Developer",
        mesValidade = 12,
        anoValidade = LocalDate.now().year + 3,
        cvv = "789",
        parcelas = 6
    )

    println("💳 Pagamento com Cartão:")
    println("• Número Mascarado: ${cartao.numeroCartao.mascararCartao()}")
    println("• Titular: ${cartao.titular}")
    println("• Parcelas: ${cartao.parcelas}x de ${cartao.valorParcela.emMoedaBRL}")

    val resultadoCartao = pagamentoService.processar(cartao)
    println("• Status: ${pagamentoService.gerarRelatorio(resultadoCartao)}")

    // Scope Function 'let': Emissão de NF-e apenas se aprovado
    val nfe = pagamentoService.emitirNotaFiscalSeAprovado(resultadoCartao)
    println("• ${nfe ?: "NF-e não gerada"}")

    println("\n--------------------------------------------------\n")

    // 2. Pagamento instantâneo via PIX
    val pix = MetodoPagamento.Pix.criarComChaveEmail(
        valor = 89.90,
        email = "cliente.vip@empresa.com",
        descricao = "E-book Kotlin Back-End Pro"
    )

    println("⚡ Pagamento com PIX:")
    println("• Chave PIX: ${pix.chavePix}")
    println("• Valor: ${pix.valor.emMoedaBRL}")

    val resultadoPix = pagamentoService.processar(pix)
    println("• Status: ${pagamentoService.gerarRelatorio(resultadoPix)}")

    // Demonstração de imutabilidade com .copy()
    if (resultadoPix is ResultadoPagamento.Pendente) {
        val pixEstendido = pagamentoService.estenderExpiracaoPix(resultadoPix, 15)
        println("• Expiração Original: ${resultadoPix.dataExpiracao.toLocalTime()}")
        println("• Expiração Prorrogada (.copy): ${pixEstendido.dataExpiracao.toLocalTime()}")
    }

    println("\n✅ Demonstração da Fase 1 e Fase 2 concluída com sucesso!")
}
