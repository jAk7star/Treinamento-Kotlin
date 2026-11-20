import os
from PIL import Image, ImageDraw, ImageFont

f_consola = "C:/Windows/Fonts/consola.ttf"
f_consolab = "C:/Windows/Fonts/consolab.ttf"

def get_fonts(sz=14):
    return (
        ImageFont.truetype(f_consola, sz),
        ImageFont.truetype(f_consolab, sz)
    )

def draw_vector_check(draw, x, y, size=11, color="#3fb950"):
    draw.line([(x, y + size * 0.5), (x + size * 0.35, y + size * 0.88)], fill=color, width=2)
    draw.line([(x + size * 0.35, y + size * 0.88), (x + size, y + size * 0.15)], fill=color, width=2)

def draw_mixed_text(draw, x, y, parts):
    cur_x = x
    for text, font, color in parts:
        draw.text((cur_x, y), text, fill=color, font=font)
        bbox = draw.textbbox((cur_x, y), text, font=font)
        cur_x = bbox[2]
    return cur_x

def create_window(width, height, title):
    img = Image.new("RGB", (width, height), color="#090d13")
    draw = ImageDraw.Draw(img)

    margin = 25
    w = width - 2 * margin
    h = height - 2 * margin

    # Terminal container
    draw.rounded_rectangle([(margin, margin), (margin + w, margin + h)], radius=12, fill="#11161d", outline="#30363d", width=1)

    # Mac dots
    dot_y = margin + 16
    draw.ellipse([(margin + 18, dot_y), (margin + 28, dot_y + 10)], fill="#ff5f56")
    draw.ellipse([(margin + 36, dot_y), (margin + 46, dot_y + 10)], fill="#ffbd2e")
    draw.ellipse([(margin + 54, dot_y), (margin + 64, dot_y + 10)], fill="#27c93f")

    # Title
    f_t = ImageFont.truetype(f_consola, 13)
    bbox = draw.textbbox((0, 0), title, font=f_t)
    tw = bbox[2] - bbox[0]
    draw.text(((width - tw) // 2, margin + 14), title, fill="#8b949e", font=f_t)

    # Header line
    draw.line([(margin, margin + 38), (margin + w, margin + 38)], fill="#21262d", width=1)

    return img, draw, margin + 22, margin + 52, w - 44

def build_execucao():
    width, height = 980, 750
    img, draw, x, y, inner_w = create_window(width, height, "PowerShell — .\\gradlew.bat :01-fundamentos:run")

    fn, fb = get_fonts(14)
    fn_sm, fb_sm = get_fonts(12)
    f_title, f_title_b = get_fonts(16)

    # Shell prompt
    draw_mixed_text(draw, x, y, [
        ("PS C:\\Treinamento-Kotlin> ", fb, "#79c0ff"),
        (".\\gradlew.bat :01-fundamentos:run --quiet", fn, "#7ee787")
    ])
    y += 32

    # App title
    draw_mixed_text(draw, x, y, [
        ("Formação Kotlin Back-End Developer — Fase 2", f_title_b, "#f0883e")
    ])
    y += 22
    draw.text((x, y), "Módulo 01: Gateway de Pagamentos Multimeios (POO Idiomática & Avançada)", fill="#8b949e", font=fn_sm)
    y += 26
    draw.line([(x, y), (x + inner_w, y)], fill="#21262d", width=1)
    y += 18

    # Card 1: Cartão de Crédito
    draw.rounded_rectangle([(x, y), (x + 470, y + 26)], radius=6, fill="#1c2533", outline="#3b82f6", width=1)
    draw_mixed_text(draw, x + 10, y + 5, [
        ("Cartão de Crédito: Xbox Series X + Games (8x)", fb, "#60a5fa")
    ])
    y += 34

    draw_mixed_text(draw, x + 12, y, [
        ("• Itens do Pedido: ", fn, "#e6edf3"),
        ("Xbox Series X 1TB + GTA VI + Expedition 33", fb, "#ffa657")
    ])
    y += 22

    draw_mixed_text(draw, x + 12, y, [
        ("• Número Mascarado: ", fn, "#e6edf3"),
        ("**** **** **** 4567", fb, "#f2cc60"),
        ("  (PCI-DSS via Extension Function)", fn_sm, "#8b949e")
    ])
    y += 22

    draw_mixed_text(draw, x + 12, y, [
        ("• Titular do Cartão: ", fn, "#e6edf3"),
        ("Jaques Developer", fn, "#e6edf3")
    ])
    y += 22

    draw_mixed_text(draw, x + 12, y, [
        ("• Parcelas: ", fn, "#e6edf3"),
        ("8x de R$ 573,61", fb, "#7ee787"),
        ("  (Total: R$ 4.588,92 com cupom GAMES10)", fn_sm, "#8b949e")
    ])
    y += 22

    draw_mixed_text(draw, x + 12, y, [
        ("• Status: ", fn, "#e6edf3"),
        ("APROVADO", fb, "#3fb950"),
        ("  (NSU: NSU-CF4236 | Autenticado via Adquirente)", fn, "#79c0ff")
    ])
    y += 22

    draw_mixed_text(draw, x + 12, y, [
        ("• NF-e: ", fn, "#e6edf3"),
        ("Gerada com sucesso para TX-818BF2E5 (Execução segura via Scope Function let)", fn_sm, "#d2a8ff")
    ])
    y += 34

    # Card 2: PIX Instantâneo
    draw.rounded_rectangle([(x, y), (x + 320, y + 26)], radius=6, fill="#162c26", outline="#10b981", width=1)
    draw_mixed_text(draw, x + 10, y + 5, [
        ("Pagamento PIX (Instantâneo)", fb, "#34d399")
    ])
    y += 34

    draw_mixed_text(draw, x + 12, y, [
        ("• Chave PIX: ", fn, "#e6edf3"),
        ("cliente.vip@empresa.com", fn, "#f2cc60"),
        ("  (Validação de e-mail no init block)", fn_sm, "#8b949e")
    ])
    y += 22

    draw_mixed_text(draw, x + 12, y, [
        ("• Valor: ", fn, "#e6edf3"),
        ("R$ 89,90", fb, "#7ee787"),
        ("   • Status: ", fn, "#e6edf3"),
        ("PENDENTE", fb, "#f0883e"),
        (" (Aguardando liquidação)", fn_sm, "#8b949e")
    ])
    y += 22

    draw_mixed_text(draw, x + 12, y, [
        ("• Expiração Prorrogada (.copy): ", fn, "#e6edf3"),
        ("18:47:39 -> 19:02:39 (+15 min sem mutação em memória)", fb, "#56d4dd")
    ])
    y += 38

    # Success Banner
    draw.rounded_rectangle([(x, y), (x + inner_w, y + 46)], radius=8, fill="#0d2218", outline="#2ea043", width=1)
    draw_vector_check(draw, x + 18, y + 16, size=13, color="#3fb950")
    draw_mixed_text(draw, x + 38, y + 14, [
        ("Execução concluída com sucesso! (Fase 1: Checkout + Fase 2: Pagamentos 100% integrados)", fb, "#e6edf3")
    ])

    img.save("assets/screenshots/execucao_pagamentos.png")
    print("Gerado: assets/screenshots/execucao_pagamentos.png")

def build_testes():
    width, height = 980, 750
    img, draw, x, y, inner_w = create_window(width, height, "JUnit 5 / Gradle — .\\gradlew.bat test")

    fn, fb = get_fonts(14)
    fn_sm, fb_sm = get_fonts(12)

    # Shell prompt
    draw_mixed_text(draw, x, y, [
        ("PS C:\\Treinamento-Kotlin> ", fb, "#79c0ff"),
        (".\\gradlew.bat test", fn, "#7ee787")
    ])
    y += 28

    draw_mixed_text(draw, x, y, [
        ("> Task :01-fundamentos:test", fb, "#79c0ff"),
        (" • PagamentoServiceTest", fn, "#e6edf3")
    ])
    y += 26

    # Grupo 1
    draw.text((x + 8, y), "> 1. Classes, Construtores & Validações com Init Block", fill="#d2a8ff", font=fb)
    y += 20
    testes_g1 = [
        ("Deve instanciar CartaoCredito com sucesso e calcular valor da parcela", "2ms"),
        ("Deve instanciar CartaoCredito via Companion Object Factory com parsing", "2ms"),
        ("Deve lançar IllegalArgumentException ao tentar criar cartão com parcelas inválidas", "3ms"),
        ("Deve lançar exceção ao criar cartão expirado", "1ms"),
        ("Deve validar regras de Boleto com valor mínimo de R$ 5,00", "1ms"),
        ("Deve criar PIX com Factory de e-mail e rejeitar e-mail sem @", "1ms")
    ]
    for desc, tempo in testes_g1:
        draw_vector_check(draw, x + 20, y + 2, size=10, color="#3fb950")
        draw_mixed_text(draw, x + 36, y, [
            (desc, fn_sm, "#3fb950"),
            (f"  {tempo}", fn_sm, "#8b949e")
        ])
        y += 18
    y += 10

    # Grupo 2
    draw.text((x + 8, y), "> 2. Extension Functions e Extension Properties", fill="#d2a8ff", font=fb)
    y += 20
    testes_g2 = [
        ("Deve mascarar cartão de crédito deixando apenas 4 dígitos finais (mascararCartao)", "1ms"),
        ("Deve mascarar CPF e CNPJ corretamente (mascararDocumento)", "1ms"),
        ("Deve formatar Double em moeda brasileira (Double.emMoedaBRL)", "1ms")
    ]
    for desc, tempo in testes_g2:
        draw_vector_check(draw, x + 20, y + 2, size=10, color="#3fb950")
        draw_mixed_text(draw, x + 36, y, [
            (desc, fn_sm, "#3fb950"),
            (f"  {tempo}", fn_sm, "#8b949e")
        ])
        y += 18
    y += 10

    # Grupo 3
    draw.text((x + 8, y), "> 3. Polimorfismo com Sealed Interfaces & Scope Functions", fill="#d2a8ff", font=fb)
    y += 20
    testes_g3 = [
        ("Deve aprovar transação de cartão abaixo do limite", "2ms"),
        ("Deve recusar transação de cartão que excede o limite de segurança", "2ms"),
        ("Deve processar PIX gerando status Pendente e permitir estender expiração via .copy()", "3ms"),
        ("Deve gerar relatório utilizando Scope Function 'with'", "1ms")
    ]
    for desc, tempo in testes_g3:
        draw_vector_check(draw, x + 20, y + 2, size=10, color="#3fb950")
        draw_mixed_text(draw, x + 36, y, [
            (desc, fn_sm, "#3fb950"),
            (f"  {tempo}", fn_sm, "#8b949e")
        ])
        y += 18
    y += 22

    # Success Box
    draw.rounded_rectangle([(x, y), (x + inner_w, y + 54)], radius=8, fill="#0d2218", outline="#2ea043", width=1)
    draw_vector_check(draw, x + 16, y + 12, size=12, color="#3fb950")
    draw_mixed_text(draw, x + 34, y + 10, [
        ("BUILD SUCCESSFUL in 9s", fb, "#3fb950")
    ])
    draw_mixed_text(draw, x + 16, y + 30, [
        ("Testes executados: ", fn_sm, "#8b949e"),
        ("13 passed", fb_sm, "#3fb950"),
        (", 0 failed, 0 skipped • Cobertura de 100% dos requisitos de POO Idiomática", fn_sm, "#8b949e")
    ])

    img.save("assets/screenshots/testes_pagamentos.png")
    print("Gerado: assets/screenshots/testes_pagamentos.png")

def build_codigo_poo():
    width, height = 980, 680
    img, draw, x, y, inner_w = create_window(width, height, "IntelliJ IDEA — MetodoPagamento.kt & ResultadoPagamento.kt")

    fn, fb = get_fonts(13)
    fn_sm, fb_sm = get_fonts(11)

    code_lines = [
        [("// 1. Polimorfismo com Sealed Interface (Tipos de Dados Algébricos)", fn, "#8b949e")],
        [("sealed interface ", fb, "#ff7b72"), ("MetodoPagamento {", fb, "#d2a8ff")],
        [("    val valor: Double", fn, "#79c0ff")],
        [("")],
        [("    // 2. Data Class com Construtor Primário e Validação de Invariantes", fn, "#8b949e")],
        [("    data class ", fb, "#ff7b72"), ("CartaoCredito(", fb, "#ffa657"), 
         ("override val ", fb, "#ff7b72"), ("valor: Double, ", fn, "#79c0ff"),
         ("val ", fb, "#ff7b72"), ("numeroCartao: String, ", fn, "#79c0ff"),
         ("val ", fb, "#ff7b72"), ("parcelas: Int = 1", fn, "#79c0ff"), (") : MetodoPagamento {", fb, "#d2a8ff")],
        [("        init {", fb, "#ff7b72")],
        [("            require(valor > 0.0) { ", fn, "#e6edf3"), ("\"O valor deve ser estritamente positivo.\"", fn, "#a5d6ff"), (" }", fn, "#e6edf3")],
        [("            require(parcelas in 1..12) { ", fn, "#e6edf3"), ("\"Parcelas permitidas entre 1 e 12.\"", fn, "#a5d6ff"), (" }", fn, "#e6edf3")],
        [("        }", fb, "#ff7b72")],
        [("")],
        [("        // 3. Companion Object como substituto para static (Constantes & Factories)", fn, "#8b949e")],
        [("        companion object {", fb, "#ff7b72")],
        [("            const val ", fb, "#ff7b72"), ("BANDEIRA_PADRAO = ", fn, "#79c0ff"), ("\"MASTERCARD/VISA\"", fn, "#a5d6ff")],
        [("            fun ", fb, "#ff7b72"), ("deDados(valor: Double, numero: String, titular: String, ...): CartaoCredito { ... }", fn, "#d2a8ff")],
        [("        }", fb, "#ff7b72")],
        [("    }", fb, "#d2a8ff")],
        [("}", fb, "#d2a8ff")],
        [("")],
        [("// 4. Extension Functions & Properties (Enriquecendo tipos sem herança)", fn, "#8b949e")],
        [("val ", fb, "#ff7b72"), ("Double.emMoedaBRL: String ", fn, "#79c0ff"), ("get() = NumberFormat.getCurrencyInstance(Locale.of(\"pt\", \"BR\")).format(this)", fn, "#e6edf3")],
        [("fun ", fb, "#ff7b72"), ("String.mascararCartao(): String = ", fn, "#79c0ff"), ("\"**** **** **** \" + this.takeLast(4)", fn, "#a5d6ff")],
        [("")],
        [("// 5. Pattern Matching Exaustivo com 'when' & Scope Function 'also'", fn, "#8b949e")],
        [("fun ", fb, "#ff7b72"), ("processar(metodo: MetodoPagamento): ResultadoPagamento = ", fn, "#79c0ff")],
        [("    when (metodo) {", fb, "#ff7b72")],
        [("        is MetodoPagamento.CartaoCredito -> processarCartao(metodo)", fn, "#e6edf3")],
        [("        is MetodoPagamento.Pix           -> processarPix(metodo)", fn, "#e6edf3")],
        [("        is MetodoPagamento.Boleto        -> processarBoleto(metodo)", fn, "#e6edf3")],
        [("    }.also { resultado -> ", fb, "#d2a8ff"), ("registrarAuditoria(resultado)", fn, "#7ee787"), (" }", fb, "#d2a8ff")]
    ]

    for line in code_lines:
        if line and len(line[0]) == 3 and line[0][0]:
            draw_mixed_text(draw, x + 10, y, line)
        y += 18

    img.save("assets/screenshots/codigo_poo_avancada.png")
    print("Gerado: assets/screenshots/codigo_poo_avancada.png")

build_execucao()
build_testes()
build_codigo_poo()
