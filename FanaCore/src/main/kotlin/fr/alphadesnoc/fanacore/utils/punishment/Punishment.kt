package fr.alphadesnoc.fanacore.utils.punishment

import java.util.UUID

data class Punishment(val uuid: UUID, val type: PunishmentType, val reason: PunishmentReason, val date: String, val dateInMillis: Long, val duration: PunishmentDuration)