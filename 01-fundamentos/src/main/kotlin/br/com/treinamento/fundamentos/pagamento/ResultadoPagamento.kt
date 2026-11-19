package br.com.treinamento.fundamentos.pagamento

import java.time.LocalDateTime

/**
 * Modela os resultados possíveis de uma transação através de uma sealed interface.
 * Em vez de criar um objeto anêmico com múltiplos campos anuláveis (nsu?, motivo?, qrCode?),
 * cada variante contém única e exclusivamente os dados consistentes para aquele estado.
 */
sealed interface ResultadoPagamento {
    val transacaoId: String
    val valor: Double
    val dataHora: LocalDateTime

    /**
     * Transação autorizada com sucesso.
     */
    data class Aprovado(
        override val transacaoId: String,
        override val valor: Double,
        override val dataHora: LocalDateTime = LocalDateTime.now(),
        val nsuAutorizacao: String,
        val comprovanteUrl: String
    ) : ResultadoPagamento

    /**
     * Transação recusada pela adquirente ou regras antifraude.
     */
    data class Recusado(
        override val transacaoId: String,
        override val valor: Double,
        override val dataHora: LocalDateTime = LocalDateTime.now(),
        val motivo: String,
        val codigoErro: String
    ) : ResultadoPagamento

    /**
     * Transação pendente de liquidação pelo pagador (PIX ou Boleto).
     */
    data class Pendente(
        override val transacaoId: String,
        override val valor: Double,
        override val dataHora: LocalDateTime = LocalDateTime.now(),
        val linhaOuCopiaCola: String,
        val qrCodeBase64: String?,
        val dataExpiracao: LocalDateTime
    ) : ResultadoPagamento
}
