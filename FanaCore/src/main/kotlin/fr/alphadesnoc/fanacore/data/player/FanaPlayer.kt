package fr.alphadesnoc.fanacore.data.player

import fr.alphadesnoc.fanacore.utils.punishment.Punishment
import fr.alphadesnoc.fanacore.utils.Rank

data class FanaPlayer(
    var money: Double = 0.0,
    var rank: Rank = Rank.MOUSSE,
    var lastFightDate: Long?,
    var punishments: MutableList<Punishment>,
    var tempBanned: Boolean = false,
    var bannedDuration: String? = null,
    var banned: Boolean = false,
)