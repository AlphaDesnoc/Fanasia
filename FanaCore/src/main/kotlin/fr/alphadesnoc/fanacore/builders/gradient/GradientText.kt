package fr.alphadesnoc.fanacore.builders.gradient

import net.md_5.bungee.api.chat.TextComponent
import java.awt.Color

class GradientText(private val text: String, private val gradientColors: List<Color>, private val blur: Double) {

    fun renderText() : String
    {
        return renderTextGradient().toLegacyText()
    }
    fun renderTextGradient(): TextComponent {
        val gradientComponents: MutableList<TextComponent> = ArrayList()

        val split = text.split(Regex("(?<=&\\w)|(?=&\\w)|(?<=.)"))

        val bold = false
        val underline = false
        val strikethrough = false
        val italic = false

        split.forEachIndexed { i, part ->
            if (!part.startsWith("&")) {
                val percent = i.toFloat() / split.size * 100.0f
                val pr = Math.round(percent)
                val interpolatedColor = interpolateColor(gradientColors, pr / 100.0, blur)
                val comp = TextComponent(part)
                comp.isBold = bold
                comp.isUnderlined = underline
                comp.isStrikethrough = strikethrough
                comp.isItalic = italic
                comp.isObfuscated = false
                comp.color = net.md_5.bungee.api.ChatColor.of("#%06x".format(interpolatedColor.rgb and 0xFFFFFF))
                gradientComponents.add(comp)
            }
        }

        val finalText = TextComponent()
        gradientComponents.forEach { finalText.addExtra(it) }
        return finalText
    }

    private fun interpolateColor(colors: List<Color>, x: Double, c: Double): Color {
        var r = 0.0
        var g = 0.0
        var b = 0.0
        var total = 0.0
        val step = 1.0 / (colors.size - 1).toDouble()
        var mu = 0.0

        for (ignored in colors) {
            total += Math.exp(-(x - mu) * (x - mu) / (2.0 * c)) / Math.sqrt(6.283185307179586 * c)
            mu += step
        }

        mu = 0.0

        for (color in colors) {
            val percent = Math.exp(-(x - mu) * (x - mu) / (2.0 * c)) / Math.sqrt(6.283185307179586 * c)
            mu += step
            r += color.red * percent / total
            g += color.green * percent / total
            b += color.blue * percent / total
        }

        return Color(r.toInt(), g.toInt(), b.toInt())
    }
}