package fr.alphadesnoc.fanacore.builders.gradient

import java.awt.Color

class GradientTextBuilder {
    private var text: String? = null
    private val gradientColors: MutableList<Color> = mutableListOf()
    private var blur: Double = 0.0

    fun text(text: String): GradientTextBuilder {
        this.text = text
        return this
    }

    fun addColor(hex: String): GradientTextBuilder {
        gradientColors.add(Color.decode(hex))
        return this
    }

    fun blur(value: Double): GradientTextBuilder {
        this.blur = value
        return this
    }

    fun build(): GradientText {
        requireNotNull(text) { "Text must be specified" }
        require(gradientColors.isNotEmpty()) { "At least one color must be added" }

        return GradientText(text!!, gradientColors, blur)
    }
}