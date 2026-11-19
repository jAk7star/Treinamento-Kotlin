package br.com.treinamento.fundamentos.pagamento

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime

@DisplayName("Fase 2: Testes Unitários de POO Idiomática & Pagamentos")
class PagamentoServiceTest {

    private val service = PagamentoService()

    @Nested
    @DisplayName("1. Classes, Construtores & Validações com Init Block")
    inner class ConstrutoresEValidados {

        @Test
        @DisplayName("Deve instanciar CartaoCredito com sucesso e calcular valor da parcela")
        fun deveInstanciarCartaoComSucesso() {
            val validade = LocalDate.now().plusYears(2)
            val cartao = MetodoPagamento.CartaoCredito(
                valor = 1200.0,
                numeroCartao = "1234567812345678",
                titular = "Ada Lovelace",
                validade = validade,
                cvv = "123",
                parcelas = 6
            )

            assertEquals(1200.0, cartao.valor)
            assertEquals(200.0, cartao.valorParcela)
            assertEquals("1234567812345678", cartao.numeroCartao)
        }

        @Test
        @DisplayName("Deve instanciar CartaoCredito via Companion Object Factory com parsing de mês/ano")
        fun deveInstanciarCartaoViaFactoryMethod() {
            val cartao = MetodoPagamento.CartaoCredito.deDados(
                valor = 500.0,
                numero = "4111222233334444",
                titular = "Alan Turing",
                mesValidade = 12,
                anoValidade = LocalDate.now().year + 2,
                cvv = "999",
                parcelas = 2
            )

            assertNotNull(cartao)
            assertEquals(250.0, cartao.valorParcela)
            assertTrue(cartao.validade.isAfter(LocalDate.now()))
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException ao tentar criar cartão com parcelas inválidas")
        fun deveFalharParcelasInvalidas() {
            val validade = LocalDate.now().plusYears(1)
            val exZero = assertThrows<IllegalArgumentException> {
                MetodoPagamento.CartaoCredito(
                    valor = 100.0,
                    numeroCartao = "1234567812345678",
                    titular = "Cliente",
                    validade = validade,
                    cvv = "123",
                    parcelas = 0
                )
            }
            assertTrue(exZero.message!!.contains("Quantidade de parcelas inválida"))

            val exMaisDe12 = assertThrows<IllegalArgumentException> {
                MetodoPagamento.CartaoCredito(
                    valor = 100.0,
                    numeroCartao = "1234567812345678",
                    titular = "Cliente",
                    validade = validade,
                    cvv = "123",
                    parcelas = 13
                )
            }
            assertTrue(exMaisDe12.message!!.contains("Quantidade de parcelas inválida"))
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar cartão expirado")
        fun deveFalharCartaoExpirado() {
            val expirado = LocalDate.now().minusDays(1)
            val ex = assertThrows<IllegalArgumentException> {
                MetodoPagamento.CartaoCredito(
                    valor = 100.0,
                    numeroCartao = "1234567812345678",
                    titular = "Cliente",
                    validade = expirado,
                    cvv = "123",
                    parcelas = 1
                )
            }
            assertTrue(ex.message!!.contains("está expirado"))
        }

        @Test
        @DisplayName("Deve validar regras de Boleto com valor mínimo de R$ 5,00")
        fun deveValidarBoleto() {
            val exValorBaixo = assertThrows<IllegalArgumentException> {
                MetodoPagamento.Boleto(
                    valor = 4.99,
                    pagadorNome = "João Silva",
                    pagadorDocumento = "12345678901"
                )
            }
            assertTrue(exValorBaixo.message!!.contains("O valor mínimo para emissão de boleto é R$ 5.0"))

            val boletoValido = MetodoPagamento.Boleto(
                valor = 150.0,
                pagadorNome = "João Silva",
                pagadorDocumento = "12345678901"
            )
            assertEquals(150.0, boletoValido.valor)
        }

        @Test
        @DisplayName("Deve criar PIX com Factory de e-mail e rejeitar e-mail sem @")
        fun deveValidarFactoryPixEmail() {
            val pixValido = MetodoPagamento.Pix.criarComChaveEmail(75.0, "dev@empresa.com", "Camisa Kotlin")
            assertEquals("dev@empresa.com", pixValido.chavePix)

            val ex = assertThrows<IllegalArgumentException> {
                MetodoPagamento.Pix.criarComChaveEmail(75.0, "emailinvalido.com")
            }
            assertTrue(ex.message!!.contains("Chave PIX de e-mail inválida"))
        }
    }

    @Nested
    @DisplayName("2. Extension Functions e Extension Properties")
    inner class ExtensionsTestes {

        @Test
        @DisplayName("Deve mascarar cartão de crédito deixando apenas 4 dígitos finais")
        fun deveMascararCartao() {
            val cartao = "4111 2222 3333 9876"
            assertEquals("**** **** **** 9876", cartao.mascararCartao())

            val cartaoSemEspaco = "5502093847581122"
            assertEquals("**** **** **** 1122", cartaoSemEspaco.mascararCartao())
        }

        @Test
        @DisplayName("Deve mascarar CPF e CNPJ corretamente")
        fun deveMascararDocumento() {
            val cpf = "123.456.789-00"
            assertEquals("123.***.***-00", cpf.mascararDocumento())

            val cnpj = "12.345.678/0001-99"
            assertEquals("12.***.***/0001-99", cnpj.mascararDocumento())
        }

        @Test
        @DisplayName("Deve formatar Double em moeda brasileira")
        fun deveFormatarEmMoedaBRL() {
            val valor = 1250.50
            val formatado = valor.emMoedaBRL
            assertTrue(formatado.contains("1.250,50") || formatado.contains("1250,50"))
            assertTrue(formatado.contains("R$"))
        }
    }

    @Nested
    @DisplayName("3. Polimorfismo com Sealed Interfaces & Scope Functions")
    inner class ProcessamentoEPoolimorfismo {

        @Test
        @DisplayName("Deve aprovar transação de cartão abaixo do limite")
        fun deveAprovarCartaoAbaixoDoLimite() {
            val cartao = MetodoPagamento.CartaoCredito.deDados(
                valor = 1500.0,
                numero = "4111222233334444",
                titular = "Grace Hopper",
                mesValidade = 10,
                anoValidade = LocalDate.now().year + 3,
                cvv = "888"
            )

            val resultado = service.processar(cartao)

            assertTrue(resultado is ResultadoPagamento.Aprovado)
            val aprovado = resultado as ResultadoPagamento.Aprovado
            assertTrue(aprovado.transacaoId.startsWith("TX-"))
            assertTrue(aprovado.nsuAutorizacao.startsWith("NSU-"))
            assertEquals(1500.0, aprovado.valor)

            // Testando Scope Function 'let' para nota fiscal
            val nfe = service.emitirNotaFiscalSeAprovado(resultado)
            assertNotNull(nfe)
            assertTrue(nfe!!.contains("NF-e gerada"))
        }

        @Test
        @DisplayName("Deve recusar transação de cartão que excede o limite de segurança")
        fun deveRecusarCartaoAcimaDoLimite() {
            val cartaoExorbitante = MetodoPagamento.CartaoCredito.deDados(
                valor = 15_000.0,
                numero = "4111222233334444",
                titular = "Golpista",
                mesValidade = 11,
                anoValidade = LocalDate.now().year + 2,
                cvv = "123"
            )

            val resultado = service.processar(cartaoExorbitante)

            assertTrue(resultado is ResultadoPagamento.Recusado)
            val recusado = resultado as ResultadoPagamento.Recusado
            assertEquals("ERR_LIMITE_EXCEDIDO", recusado.codigoErro)
            assertTrue(recusado.motivo.contains("limite transacional"))

            // Nota fiscal não deve ser gerada via 'let'
            val nfe = service.emitirNotaFiscalSeAprovado(resultado)
            assertNull(nfe)
        }

        @Test
        @DisplayName("Deve processar PIX gerando status Pendente e permitir estender expiração via .copy()")
        fun deveProcessarPixEEstenderViaCopy() {
            val pix = MetodoPagamento.Pix.criarComChaveEmail(
                valor = 99.90,
                email = "pagador@gmail.com",
                descricao = "Assinatura Mensal"
            )

            val resultado = service.processar(pix)

            assertTrue(resultado is ResultadoPagamento.Pendente)
            val pendente = resultado as ResultadoPagamento.Pendente
            assertTrue(pendente.linhaOuCopiaCola.contains("BR.GOV.BCB.PIX"))
            assertNotNull(pendente.qrCodeBase64)

            // Uso do .copy() para atualizar data sem mutação
            val expiracaoOriginal = pendente.dataExpiracao
            val estendido = service.estenderExpiracaoPix(pendente, 10)

            assertEquals(pendente.transacaoId, estendido.transacaoId)
            assertEquals(pendente.linhaOuCopiaCola, estendido.linhaOuCopiaCola)
            assertEquals(expiracaoOriginal.plusMinutes(10), estendido.dataExpiracao)
            assertNotSame(pendente, estendido) // Nova instância imutável garantida
        }

        @Test
        @DisplayName("Deve gerar relatório utilizando Scope Function 'with'")
        fun deveGerarRelatorioComWith() {
            val aprovado = ResultadoPagamento.Aprovado(
                transacaoId = "TX-TESTE1",
                valor = 250.0,
                dataHora = LocalDateTime.now(),
                nsuAutorizacao = "NSU-998877",
                comprovanteUrl = "https://comprovante.com/1"
            )

            val relatorio = service.gerarRelatorio(aprovado)
            assertTrue(relatorio.contains("TX-TESTE1"))
            assertTrue(relatorio.contains("APROVADO (NSU: NSU-998877)"))
        }
    }
}
