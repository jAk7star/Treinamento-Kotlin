package br.com.treinamento.fundamentos

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MainTest {

    @Test
    fun `deve validar configuracao do ambiente de testes kotlin`() {
        val linguagem = "Kotlin"
        val status = "Pronto para aprender"

        assertEquals("Kotlin", linguagem)
        assertTrue(status.isNotBlank())
    }
}
