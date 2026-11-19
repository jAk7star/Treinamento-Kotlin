package br.com.treinamento.fundamentos.pagamento

import java.text.NumberFormat
import java.util.Locale

/**
 * Extension Functions e Properties demonstrando enriquecimento de tipos existentes
 * sem recorrer a herança ou classes utilitárias artificiais (ex: StringUtils).
 */

/**
 * Extension Property para formatar valores monetários no padrão brasileiro (R$ 1.250,50).
 */
val Double.emMoedaBRL: String
    get() {
        val formatador = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"))
        return formatador.format(this)
    }

/**
 * Extension Function para mascarar o número do cartão de crédito preservando apenas
 * os últimos 4 dígitos (conformidade PCI-DSS).
 *
 * Exemplo: "4111111111111234".mascararCartao() -> "**** **** **** 1234"
 */
fun String.mascararCartao(): String {
    val digitos = this.replace(Regex("[^0-9]"), "")
    if (digitos.length < 4) return "****"
    val ultimos4 = digitos.takeLast(4)
    return "**** **** **** $ultimos4"
}

/**
 * Extension Function para mascarar documentos CPF/CNPJ.
 */
fun String.mascararDocumento(): String {
    val digitos = this.replace(Regex("[^0-9]"), "")
    return when (digitos.length) {
        11 -> "${digitos.substring(0, 3)}.***.***-${digitos.substring(9)}"
        14 -> "${digitos.substring(0, 2)}.***.***/${digitos.substring(8, 12)}-${digitos.substring(12)}"
        else -> "***"
    }
}
