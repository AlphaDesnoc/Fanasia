package fr.alphadesnoc.fanacore.utils

enum class Calc(val operation: (Double, Double) -> Double)
{
    ADDITION({ a, b -> a + b }),
    SUBTRACTION({ a, b -> a - b }),
    MULTIPLICATION({ a, b -> a * b }),
    DIVISION({ a, b -> if (b != 0.0) a / b else throw ArithmeticException("Division by zero.") });

    fun execute(a: Double, b: Double): Double = operation(a, b)

    companion object
    {
        fun getCalcByOpe(opeChar: Char): Calc?
        {
            return when (opeChar) {
                '+' -> ADDITION
                '-' -> SUBTRACTION
                '*' -> MULTIPLICATION
                '/' -> DIVISION
                else -> null
            }
        }

        fun convertToMilliseconds(days: Int, hours: Int, minutes: Int): Long {
            val millisInAMinute: Long = 60000
            val millisInAnHour = millisInAMinute * 60
            val millisInADay = millisInAnHour * 24

            return (days * millisInADay) + (hours * millisInAnHour) + (minutes * millisInAMinute)
        }
    }
}