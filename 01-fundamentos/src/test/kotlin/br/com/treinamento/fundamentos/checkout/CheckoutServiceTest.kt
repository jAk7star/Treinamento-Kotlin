package br.com.treinamento.fundamentos.checkout

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Testes do CheckoutService - Fundamentos de Kotlin")
class CheckoutServiceTest {

    private lateinit var service: CheckoutService

    @BeforeEach
    fun setUp() {
        service = CheckoutService()
    }

    @Nested
    @DisplayName("Cálculo de Subtotal")
    inner class SubtotalTests {

        @Test
        @DisplayName("Deve somar corretamente o subtotal de múltiplos itens")
        fun deveSomarItensCorretamente() {
            val itens = listOf(
                ItemPedido(nome = "Teclado Mecânico", precoUnitario = 200.0, quantidade = 1),
                ItemPedido(nome = "Mousepad Gamer", precoUnitario = 50.0, quantidade = 2)
            )

            val subtotal = service.calcularSubtotal(itens)

            assertEquals(300.0, subtotal)
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando lista de itens for vazia")
        fun deveFalharQuandoListaForVazia() {
            val excecao = assertThrows<IllegalArgumentException> {
                service.calcularSubtotal(emptyList())
            }

            assertTrue(excecao.message!!.contains("ao menos um item"))
        }
    }

    @Nested
    @DisplayName("Null Safety com Cupom e Elvis Operator")
    inner class DescontoTests {

        @Test
        @DisplayName("Deve aplicar desconto quando o cupom for informado")
        fun deveAplicarDescontoComCupomPresente() {
            val cupom = Cupom(codigo = "PROMO10", percentualDesconto = 0.10)
            val subtotal = 200.0

            val desconto = service.calcularDesconto(subtotal, cupom)

            assertEquals(20.0, desconto)
        }

        @Test
        @DisplayName("Deve retornar 0.0 de desconto com Elvis operator quando cupom for null")
        fun deveRetornarZeroQuandoCupomForNulo() {
            val cupom: Cupom? = null
            val subtotal = 200.0

            val desconto = service.calcularDesconto(subtotal, cupom)

            assertEquals(0.0, desconto)
        }
    }

    @Nested
    @DisplayName("Cálculo de Frete e Faixas de Valor com 'when'")
    inner class FreteTests {

        private val enderecoPadrao = Endereco(
            logradouro = "Av. Paulista, 1000",
            cidade = "São Paulo",
            uf = "SP",
            cep = "01310-100"
        )

        @Test
        @DisplayName("Deve isentar frete quando endereco for null (compra digital ou retirada)")
        fun deveRetornarFreteZeroQuandoEnderecoNulo() {
            val frete = service.calcularFrete(subtotal = 50.0, endereco = null)
            assertEquals(0.0, frete)
        }

        @Test
        @DisplayName("Deve cobrar R$ 25 de frete para subtotal menor que R$ 100")
        fun deveCobrarFreteCheioAbaixoDeCem() {
            val frete = service.calcularFrete(subtotal = 89.90, endereco = enderecoPadrao)
            assertEquals(25.0, frete)
        }

        @Test
        @DisplayName("Deve cobrar R$ 15 de frete no range de R$ 100 a R$ 299.99")
        fun deveCobrarFreteComDescontoNoRange() {
            val frete = service.calcularFrete(subtotal = 150.0, endereco = enderecoPadrao)
            assertEquals(15.0, frete)
        }

        @Test
        @DisplayName("Deve conceder frete grátis para pedidos a partir de R$ 300")
        fun deveConcederFreteGratisAcimaDeTrezentos() {
            val frete = service.calcularFrete(subtotal = 350.0, endereco = enderecoPadrao)
            assertEquals(0.0, frete)
        }
    }

    @Nested
    @DisplayName("Exaustividade de 'when' com Enum")
    inner class StatusTests {

        @Test
        @DisplayName("Deve retornar a mensagem correta para cada status do Enum")
        fun deveRetornarMensagemEsperadaPorStatus() {
            val msgCriado = service.obterMensagemStatus(StatusPedido.CRIADO)
            val msgProcessando = service.obterMensagemStatus(StatusPedido.PROCESSANDO)
            val msgAprovado = service.obterMensagemStatus(StatusPedido.APROVADO)
            val msgCancelado = service.obterMensagemStatus(StatusPedido.CANCELADO)

            assertTrue(msgCriado.contains("aguardando pagamento"))
            assertTrue(msgProcessando.contains("Preparando separação"))
            assertTrue(msgAprovado.contains("pronto para envio"))
            assertTrue(msgCancelado.contains("cancelado"))
        }
    }

    @Nested
    @DisplayName("Processamento Completo de Checkout")
    inner class CheckoutGeralTests {

        @Test
        @DisplayName("Deve calcular corretamente o resumo financeiro com desconto e frete")
        fun deveProcessarCheckoutCompleto() {
            val pedido = Pedido(
                id = "PED-001",
                itens = listOf(
                    ItemPedido(nome = "Headset", precoUnitario = 150.0, quantidade = 1)
                ),
                enderecoEntrega = Endereco(
                    logradouro = "Rua das Flores, 50",
                    cidade = "Curitiba",
                    uf = "PR",
                    cep = "80000-000"
                ),
                cupom = Cupom(codigo = "BLACK10", percentualDesconto = 0.10)
            )

            val resumo = service.processarCheckout(pedido)

            // Subtotal: 150.0 | Desconto: 15.0 (10%) | Frete: 15.0 (range 100..299.99) | Total: 150.0
            assertEquals("PED-001", resumo.pedidoId)
            assertEquals(150.0, resumo.subtotal)
            assertEquals(15.0, resumo.desconto)
            assertEquals(15.0, resumo.frete)
            assertEquals(150.0, resumo.totalFinal)
            assertEquals(StatusPedido.PROCESSANDO, resumo.status)
        }
    }
}
