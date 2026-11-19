package br.com.treinamento.fundamentos.pagamento

import java.time.LocalDateTime
import java.util.UUID

/**
 * Serviço de processamento de pagamentos.
 * Demonstra a integração entre:
 * - Sealed Interfaces & Pattern Matching exaustivo com when
 * - Scope Functions (`also`, `let`, `apply`, `run`, `with`)
 * - Imutabilidade e manipulação de estado via .copy(...)
 */
class PagamentoService {

    companion object {
        const val LIMITE_TRANSACIONAL_CARTAO = 10_000.0
    }

    /**
     * Processa qualquer MetodoPagamento de forma polimórfica e segura.
     */
    fun processar(metodo: MetodoPagamento): ResultadoPagamento {
        val transacaoId = "TX-${UUID.randomUUID().toString().substring(0, 8).uppercase()}"

        // When exaustivo garantido pelo compilador: dispensa 'else' frágil
        val resultado = when (metodo) {
            is MetodoPagamento.CartaoCredito -> processarCartao(transacaoId, metodo)
            is MetodoPagamento.Pix -> processarPix(transacaoId, metodo)
            is MetodoPagamento.Boleto -> processarBoleto(transacaoId, metodo)
        }

        // Scope Function 'also': executa ações secundárias (side-effects) mantendo o objeto original
        return resultado.also { res ->
            registrarAuditoria(res)
        }
    }

    private fun processarCartao(transacaoId: String, cartao: MetodoPagamento.CartaoCredito): ResultadoPagamento {
        return if (cartao.valor > LIMITE_TRANSACIONAL_CARTAO) {
            ResultadoPagamento.Recusado(
                transacaoId = transacaoId,
                valor = cartao.valor,
                motivo = "Valor excede o limite transacional de segurança (${LIMITE_TRANSACIONAL_CARTAO.emMoedaBRL}).",
                codigoErro = "ERR_LIMITE_EXCEDIDO"
            )
        } else {
            val nsu = "NSU-${UUID.randomUUID().toString().substring(0, 6).uppercase()}"
            ResultadoPagamento.Aprovado(
                transacaoId = transacaoId,
                valor = cartao.valor,
                nsuAutorizacao = nsu,
                comprovanteUrl = "https://checkout.pagamentos.com/recibos/$transacaoId"
            )
        }
    }

    private fun processarPix(transacaoId: String, pix: MetodoPagamento.Pix): ResultadoPagamento {
        val payloadPix = "00020126580014BR.GOV.BCB.PIX0114${pix.chavePix}520400005303986540${pix.valor}5802BR5913TREINAMENTO6009SAO PAULO62070503***6304"
        val expiracao = LocalDateTime.now().plusMinutes(pix.minutosExpiracao.toLong())

        return ResultadoPagamento.Pendente(
            transacaoId = transacaoId,
            valor = pix.valor,
            linhaOuCopiaCola = payloadPix,
            qrCodeBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
            dataExpiracao = expiracao
        )
    }

    private fun processarBoleto(transacaoId: String, boleto: MetodoPagamento.Boleto): ResultadoPagamento {
        val linhaDigitavel = "34191.79001 01043.510047 91020.150008 8 987600000${(boleto.valor * 100).toLong()}"
        val vencimento = LocalDateTime.now().plusDays(boleto.diasParaVencimento.toLong())

        return ResultadoPagamento.Pendente(
            transacaoId = transacaoId,
            valor = boleto.valor,
            linhaOuCopiaCola = linhaDigitavel,
            qrCodeBase64 = null,
            dataExpiracao = vencimento
        )
    }

    /**
     * Demonstração prática da Scope Function 'let':
     * Executa a emissão de nota fiscal APENAS se o pagamento for uma aprovação.
     */
    fun emitirNotaFiscalSeAprovado(resultado: ResultadoPagamento): String? {
        return (resultado as? ResultadoPagamento.Aprovado)?.let { aprovado ->
            "NF-e gerada para a transação ${aprovado.transacaoId} no valor de ${aprovado.valor.emMoedaBRL} (NSU: ${aprovado.nsuAutorizacao})"
        }
    }

    /**
     * Demonstração prática da Scope Function 'with':
     * Agrupa operações e leituras de um mesmo receptor para gerar um relatório textual.
     */
    fun gerarRelatorio(resultado: ResultadoPagamento): String = with(resultado) {
        val tipoFormatado = when (this) {
            is ResultadoPagamento.Aprovado -> "APROVADO (NSU: $nsuAutorizacao)"
            is ResultadoPagamento.Recusado -> "RECUSADO (Motivo: $motivo)"
            is ResultadoPagamento.Pendente -> "PENDENTE (Expira em: ${dataExpiracao.toLocalTime()})"
        }
        "Relatório [$transacaoId]: Valor = ${valor.emMoedaBRL} | Status = $tipoFormatado"
    }

    /**
     * Demonstração de .copy() em Data Classes para alteração sem mutação:
     * Simula a concessão de mais 10 minutos para pagamento de um PIX pendente.
     */
    fun estenderExpiracaoPix(pendente: ResultadoPagamento.Pendente, minutosAdicionais: Long): ResultadoPagamento.Pendente {
        return pendente.copy(
            dataExpiracao = pendente.dataExpiracao.plusMinutes(minutosAdicionais)
        )
    }

    private fun registrarAuditoria(resultado: ResultadoPagamento) {
        val detalhe = when (resultado) {
            is ResultadoPagamento.Aprovado -> "NSU=${resultado.nsuAutorizacao}"
            is ResultadoPagamento.Recusado -> "Cod=${resultado.codigoErro}"
            is ResultadoPagamento.Pendente -> "Linha=${resultado.linhaOuCopiaCola.take(20)}..."
        }
        println("📝 [AUDIT LOG] ${resultado.transacaoId} | ${resultado.valor.emMoedaBRL} | ${resultado::class.simpleName} | $detalhe")
    }
}
