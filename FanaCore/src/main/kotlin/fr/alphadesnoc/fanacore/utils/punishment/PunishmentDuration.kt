package fr.alphadesnoc.fanacore.utils.punishment

import fr.alphadesnoc.fanacore.utils.Calc

enum class PunishmentDuration(val format: String, val time: Long) {

    MIN15("15m", Calc.convertToMilliseconds(0, 0, 15)),
    MIN30("30m", Calc.convertToMilliseconds(0, 0, 30)),
    HOUR1("1h", Calc.convertToMilliseconds(0, 1, 0)),
    HOUR2("2h", Calc.convertToMilliseconds(0, 2, 0)),
    HOUR3("3h", Calc.convertToMilliseconds(0, 3, 0)),
    DAY1("1j", Calc.convertToMilliseconds(1, 0, 0)),
    DAY3("3j", Calc.convertToMilliseconds(3, 0, 0)),
    DAY7("7j", Calc.convertToMilliseconds(7, 0, 0)),
    DAY10("10j", Calc.convertToMilliseconds(10, 0, 0)),
    DAY20("20j", Calc.convertToMilliseconds(20, 0, 0)),
    DAY30("30j", Calc.convertToMilliseconds(30, 0, 0)),
    INFINITE("A vie", Calc.convertToMilliseconds(0, 0, 0)),
    NULL("Aucune Durée", Calc.convertToMilliseconds(0, 0, 0))

}