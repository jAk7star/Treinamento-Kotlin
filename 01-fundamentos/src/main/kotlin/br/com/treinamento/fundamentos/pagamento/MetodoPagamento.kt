package br.com.treinamento.fundamentos.pagamento

import java.time.LocalDate

/**
 * Representa a abstração selada dos métodos de pagamento suportados pelo Gateway.
 * Utiliza sealed interface para modelar tipos de dados algébricos (ADT), garantindo
 * que todas as variantes sejam conhecidas em tempo de compilação.
 */
sealed interface MetodoPagamento {
    val valor: Double

    /**
     * Pagamento via Cartão de Crédito.
     *
     * Validações de integridade no bloco init (Invariantes de Domínio):
     * - Valor estritamente positivo
     * - Número do cartão com 16 dígitos numéricos
     * - Nome do titular obrigatório
     * - Data de validade no futuro
     * - CVV com 3 ou 4 dígitos
     * - Parcelas permitidas entre 1 e 12
     */
    data class CartaoCredito(
        override val valor: Double,
        val numeroCartao: String,
        val titular: String,
        val validade: LocalDate,
        val cvv: String,
        val parcelas: Int = 1
    ) : MetodoPagamento {
        init {
            require(valor > 0.0) { "O valor do pagamento deve ser estritamente positivo. Informado: $valor" }
            require(numeroCartao.replace(" ", "").length == 16) { "Número de cartão inválido: deve conter 16 dígitos." }
            require(titular.isNotBlank()) { "O nome do titular do cartão não pode estar em branco." }
            require(validade.isAfter(LocalDate.now())) { "O cartão de crédito informado está expirado ($validade)." }
            require(cvv.length in 3..4) { "CVV inválido: deve conter 3 ou 4 dígitos." }
            require(parcelas in 1..12) { "Quantidade de parcelas inválida ($parcelas): permitido entre 1 e 12." }
        }

        // Single-expression property para cálculo do valor individual da parcela
        val valorParcela: Double get() = valor / parcelas

        companion object {
            const val BANDEIRA_PADRAO = "MASTERCARD/VISA"

            /**
             * Factory method de conveniência que encapsula o parsing de mês/ano para LocalDate.
             */
            fun deDados(
                valor: Double,
                numero: String,
                titular: String,
                mesValidade: Int,
                anoValidade: Int,
                cvv: String,
                parcelas: Int = 1
            ): CartaoCredito {
                val dataValidade = LocalDate.of(anoValidade, mesValidade, 1)
                    .plusMonths(1)
                    .minusDays(1)
                return CartaoCredito(valor, numero, titular, dataValidade, cvv, parcelas)
            }
        }
    }

    /**
     * Pagamento instantâneo via PIX.
     *
     * Validações de integridade no bloco init:
     * - Chave PIX não pode ser em branco
     * - Tempo de expiração entre 5 e 60 minutos
     */
    data class Pix(
        override val valor: Double,
        val chavePix: String,
        val descricao: String? = null,
        val minutosExpiracao: Int = MINUTOS_EXPIRACAO_PADRAO
    ) : MetodoPagamento {
        init {
            require(valor > 0.0) { "O valor do PIX deve ser maior que zero." }
            require(chavePix.isNotBlank()) { "A chave PIX não pode estar vazia." }
            require(minutosExpiracao in 5..60) { "O tempo de expiração do PIX deve estar entre 5 e 60 minutos." }
        }

        companion object {
            const val MINUTOS_EXPIRACAO_PADRAO = 15

            /**
             * Factory method que valida e cria um pagamento PIX baseado em chave e-mail.
             */
            fun criarComChaveEmail(valor: Double, email: String, descricao: String? = null): Pix {
                require(email.contains("@")) { "Chave PIX de e-mail inválida: $email" }
                return Pix(valor = valor, chavePix = email, descricao = descricao)
            }
        }
    }

    /**
     * Pagamento via Boleto Bancário.
     *
     * Validações de integridade no bloco init:
     * - Valor mínimo de R$ 5,00 para compensação
     * - Documento com 11 (CPF) ou 14 (CNPJ) dígitos
     */
    data class Boleto(
        override val valor: Double,
        val pagadorNome: String,
        val pagadorDocumento: String,
        val diasParaVencimento: Int = DIAS_VENCIMENTO_PADRAO
    ) : MetodoPagamento {
        init {
            require(valor >= VALOR_MINIMO_BOLETO) {
                "O valor mínimo para emissão de boleto é R$ $VALOR_MINIMO_BOLETO. Informado: $valor"
            }
            require(pagadorNome.isNotBlank()) { "Nome do pagador é obrigatório para emissão do boleto." }
            val digitosDoc = pagadorDocumento.replace(Regex("[^0-9]"), "")
            require(digitosDoc.length in listOf(11, 14)) {
                "Documento do pagador deve ser CPF (11 dígitos) ou CNPJ (14 dígitos)."
            }
        }

        companion object {
            const val DIAS_VENCIMENTO_PADRAO = 3
            const val VALOR_MINIMO_BOLETO = 5.0
        }
    }
}
