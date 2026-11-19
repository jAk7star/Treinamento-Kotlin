package br.com.treinamento.fundamentos.checkout

class CheckoutService {

    /**
     * Calcula o subtotal somando o valor de cada item da lista.
     * Utiliza a função de ordem superior 'sumOf' da standard library do Kotlin.
     */
    fun calcularSubtotal(itens: List<ItemPedido>): Double {
        require(itens.isNotEmpty()) { "O pedido deve conter ao menos um item." }
        return itens.sumOf { it.total() }
    }

    /**
     * Calcula o desconto com Null Safety:
     * - 'cupom?.percentualDesconto': Safe call que retorna o desconto ou null se o cupom não existir.
     * - '?: 0.0': Operador Elvis que define taxa 0 caso o cupom seja nulo.
     */
    fun calcularDesconto(subtotal: Double, cupom: Cupom?): Double {
        val percentual = cupom?.percentualDesconto ?: 0.0
        return subtotal * percentual
    }

    /**
     * Calcula o frete combinando Null Safety e a expressão 'when':
     * - Se o endereço for nulo (ex: produto digital ou retirada), frete é 0.0.
     * - Se houver endereço, o frete é calculado por faixas de valor com 'when' como expressão.
     */
    fun calcularFrete(subtotal: Double, endereco: Endereco?): Double {
        // Se não há endereço de entrega cadastrado, não incide frete
        if (endereco == null) return 0.0

        // 'when' como expressão: retorna o valor diretamente para a variável 'valorBase'
        return when {
            subtotal <= 0.0 -> 0.0
            subtotal < 100.0 -> 25.0 // Pedidos abaixo de R$ 100: frete fixo de R$ 25
            subtotal in 100.0..299.99 -> 15.0 // Pedidos entre R$ 100 e R$ 299.99: frete com desconto (R$ 15)
            else -> 0.0 // Pedidos a partir de R$ 300: frete grátis!
        }
    }

    /**
     * Determina a mensagem de status utilizando 'when' com Enum.
     * Como 'StatusPedido' é um enum e todos os 4 casos estão mapeados,
     * o compilador NÃO exige o bloco 'else'.
     */
    fun obterMensagemStatus(status: StatusPedido): String = when (status) {
        StatusPedido.CRIADO -> "Pedido registrado aguardando pagamento."
        StatusPedido.PROCESSANDO -> "Pagamento recebido. Preparando separação."
        StatusPedido.APROVADO -> "Pedido aprovado e pronto para envio."
        StatusPedido.CANCELADO -> "Pedido cancelado pelo cliente ou operadora."
    }

    /**
     * Orquestra o processamento completo do checkout e consolida os valores em um ResumoCheckout.
     */
    fun processarCheckout(pedido: Pedido): ResumoCheckout {
        val subtotal = calcularSubtotal(pedido.itens)
        val desconto = calcularDesconto(subtotal, pedido.cupom)
        val frete = calcularFrete(subtotal, pedido.enderecoEntrega)
        val totalFinal = (subtotal - desconto) + frete

        return ResumoCheckout(
            pedidoId = pedido.id,
            subtotal = subtotal,
            desconto = desconto,
            frete = frete,
            totalFinal = totalFinal,
            status = StatusPedido.PROCESSANDO
        )
    }
}
