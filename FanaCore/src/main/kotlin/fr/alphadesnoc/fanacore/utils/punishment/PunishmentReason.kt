package fr.alphadesnoc.fanacore.utils.punishment

sealed class PunishmentReason {
    data class Warn(val desc: String) : PunishmentReason() {}
    data class Mute(val desc: String) : PunishmentReason() {}
    data class Ban(val desc: String) : PunishmentReason() {}
    data class TempBan(val desc: String) : PunishmentReason() {}
    data class Kick(val desc: String) : PunishmentReason() {}
}
