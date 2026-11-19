package br.com.treinamento.fundamentos.checkout

/**
 * Status do ciclo de vida do pedido.
 * O compilador utiliza este enum no 'when' para garantir exaustividade sem precisar de 'else'.
 */
enum class StatusPedido {
    CRIADO,
    PROCESSANDO,
    APROVADO,
    CANCELADO
}

/**
 * Representa o endereço de entrega do cliente.
 * Todas as propriedades são 'val' (imutáveis), prevenindo mutações acidentais após a criação.
 */
data class Endereco(
    val logradouro: String,
    val cidade: String,
    val uf: String,
    val cep: String
)

/**
 * Cupom de desconto que pode ser aplicado a um pedido.
 */
data class Cupom(
    val codigo: String,
    val percentualDesconto: Double // Ex: 0.10 para 10%
)

/**
 * Item individual pertencente a um pedido.
 */
data class ItemPedido(
    val nome: String,
    val precoUnitario: Double,
    val quantidade: Int
) {
    // Single-expression function para calcular o total do item
    fun total(): Double = precoUnitario * quantidade
}

/**
 * Entidade principal do Pedido.
 * Demonstração de Null Safety:
 * - 'itens' e 'status': Não-anuláveis (obrigatórios)
 * - 'enderecoEntrega': Anulável (pode ser compra digital ou retirada sem frete)
 * - 'cupom': Anulável (o cliente pode não ter cupom)
 */
data class Pedido(
    val id: String,
    val itens: List<ItemPedido>,
    val enderecoEntrega: Endereco? = null,
    val cupom: Cupom? = null,
    val status: StatusPedido = StatusPedido.CRIADO
)

/**
 * DTO que encapsula o resultado financeiro consolidado do checkout.
 */
data class ResumoCheckout(
    val pedidoId: String,
    val subtotal: Double,
    val desconto: Double,
    val frete: Double,
    val totalFinal: Double,
    val status: StatusPedido
)
