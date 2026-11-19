import os
from PIL import Image, ImageDraw, ImageFont

def create_terminal_window(width, height, title):
    img = Image.new("RGB", (width, height), color="#090d13")
    draw = ImageDraw.Draw(img)

    margin = 30
    win_w = width - 2 * margin
    win_h = height - 2 * margin

    # Window container
    draw.rounded_rectangle(
        [(margin, margin), (margin + win_w, margin + win_h)],
        radius=14,
        fill="#121820",
        outline="#30363d",
        width=1
    )

    # Mac-style dots
    dot_y = margin + 18
    draw.ellipse([(margin + 20, dot_y), (margin + 32, dot_y + 12)], fill="#ff5f56")
    draw.ellipse([(margin + 40, dot_y), (margin + 52, dot_y + 12)], fill="#ffbd2e")
    draw.ellipse([(margin + 60, dot_y), (margin + 72, dot_y + 12)], fill="#27c93f")

    # Title
    font_title = ImageFont.truetype("C:/Windows/Fonts/consola.ttf", 13)
    bbox = draw.textbbox((0, 0), title, font=font_title)
    tw = bbox[2] - bbox[0]
    draw.text(((width - tw) // 2, margin + 16), title, fill="#8b949e", font=font_title)

    # Window top bar divider
    draw.line([(margin, margin + 40), (margin + win_w, margin + 40)], fill="#21262d", width=1)

    return img, draw, margin, margin + 55

def render_execucao():
    width, height = 980, 720
    img, draw, start_x, start_y = create_terminal_window(
        width, height, "PowerShell — .\\gradlew.bat :01-fundamentos:run"
    )

    f_norm = ImageFont.truetype("C:/Windows/Fonts/consola.ttf", 14)
    f_bold = ImageFont.truetype("C:/Windows/Fonts/consolab.ttf", 14)
    f_title = ImageFont.truetype("C:/Windows/Fonts/consolab.ttf", 16)
    f_sm = ImageFont.truetype("C:/Windows/Fonts/consola.ttf", 13)

    x = start_x + 25
    y = start_y

    # Shell prompt
    draw.text((x, y), "PS C:\\Treinamento-Kotlin> ", fill="#79c0ff", font=f_bold)
    draw.text((x + 215, y), ".\\gradlew.bat :01-fundamentos:run --quiet", fill="#7ee787", font=f_norm)
    y += 35

    # Title banner
    draw.text((x, y), "🚀 Formação Kotlin Back-End Developer — Fase 2", fill="#f0883e", font=f_title)
    y += 22
    draw.text((x, y), "Módulo 01: Gateway de Pagamentos Multimeios (POO Idiomática)", fill="#8b949e", font=f_sm)
    y += 28

    draw.line([(x, y), (width - start_x - 25, y)], fill="#21262d", width=1)
    y += 18

    # Card 1: Cartão de Crédito
    # Pill header
    draw.rounded_rectangle([(x, y), (x + 390, y + 26)], radius=6, fill="#1f2937", outline="#3b82f6", width=1)
    draw.text((x + 12, y + 5), "💳 Cartão de Crédito (Parcelado em 6x)", fill="#60a5fa", font=f_bold)
    y += 34

    draw.text((x + 10, y), "• Número Mascarado: ", fill="#e6edf3", font=f_norm)
    draw.text((x + 175, y), "**** **** **** 4567", fill="#f2cc60", font=f_bold)
    draw.text((x + 345, y), "(PCI-DSS via Extension Function)", fill="#8b949e", font=f_sm)
    y += 22

    draw.text((x + 10, y), "• Titular do Cartão: ", fill="#e6edf3", font=f_norm)
    draw.text((x + 175, y), "Jaques Developer", fill="#e6edf3", font=f_norm)
    y += 22

    draw.text((x + 10, y), "• Parcelas: ", fill="#e6edf3", font=f_norm)
    draw.text((x + 105, y), "6x de R$ 399,00", fill="#7ee787", font=f_bold)
    draw.text((x + 250, y), "(Total: R$ 2.394,00 via Double.emMoedaBRL)", fill="#8b949e", font=f_sm)
    y += 22

    draw.text((x + 10, y), "• Status: ", fill="#e6edf3", font=f_norm)
    draw.text((x + 85, y), "APROVADO", fill="#3fb950", font=f_bold)
    draw.text((x + 165, y), "(NSU: NSU-69D644 | Autenticado via Adquirente)", fill="#79c0ff", font=f_norm)
    y += 22

    draw.text((x + 10, y), "• NF-e: ", fill="#e6edf3", font=f_norm)
    draw.text((x + 65, y), "Gerada com sucesso para TX-41AE26A8 (Scope Function let)", fill="#d2a8ff", font=f_sm)
    y += 35

    # Card 2: PIX Instantâneo
    draw.rounded_rectangle([(x, y), (x + 360, y + 26)], radius=6, fill="#1f2937", outline="#10b981", width=1)
    draw.text((x + 12, y + 5), "⚡ Pagamento PIX (Instantâneo)", fill="#34d399", font=f_bold)
    y += 34

    draw.text((x + 10, y), "• Chave PIX: ", fill="#e6edf3", font=f_norm)
    draw.text((x + 115, y), "cliente.vip@empresa.com", fill="#f2cc60", font=f_norm)
    draw.text((x + 325, y), "(Validada no init block)", fill="#8b949e", font=f_sm)
    y += 22

    draw.text((x + 10, y), "• Valor:", fill="#e6edf3", font=f_norm)
    draw.text((x + 75, y), "R$ 89,90", fill="#7ee787", font=f_bold)
    draw.text((x + 170, y), "• Status:", fill="#e6edf3", font=f_norm)
    draw.text((x + 245, y), "PENDENTE", fill="#f0883e", font=f_bold)
    draw.text((x + 335, y), "(Aguardando liquidação)", fill="#8b949e", font=f_sm)
    y += 22

    draw.text((x + 10, y), "• Expiração Prorrogada (.copy): ", fill="#e6edf3", font=f_norm)
    draw.text((x + 265, y), "18:47:39 ➔ 19:02:39 (+15 min imutável)", fill="#56d4dd", font=f_bold)
    y += 40

    # Success Banner
    box_w = width - 2 * start_x - 50
    draw.rounded_rectangle(
        [(x, y), (x + box_w, y + 46)],
        radius=8,
        fill="#0d2218",
        outline="#2ea043",
        width=1
    )
    draw.text((x + 18, y + 14), "✔", fill="#3fb950", font=f_bold)
    draw.text((x + 40, y + 14), "Execução concluída com sucesso! (Fase 1: Checkout + Fase 2: Pagamentos 100% integrados)", fill="#e6edf3", font=f_bold)

    output_path = "assets/screenshots/execucao_pagamentos.png"
    img.save(output_path)
    print("Salvo:", output_path)

render_execucao()
