package fr.alphadesnoc.fanamod.events

import fr.alphadesnoc.fanacore.FanaCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatEvent : Listener
{

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (FanaCore.mutedPlayer.containsKey(player.name)) {
            if (FanaCore.mutedPlayer[player.name]!! <= System.currentTimeMillis()) {
                FanaCore.mutedPlayer.remove(player.name)
            }
            else {
                event.isCancelled = true
                player.sendMessage("Vous êtes mute pendant encore ${formatMilliseconds(FanaCore.mutedPlayer[player.name]!! - System.currentTimeMillis())}")
            }
        }

    }

    private fun formatMilliseconds(millis: Long): String {
        val totalSeconds = millis / 1000
        val seconds = totalSeconds % 60
        val totalMinutes = totalSeconds / 60
        val minutes = totalMinutes % 60
        val totalHours = totalMinutes / 60
        val hours = totalHours % 24
        val days = totalHours / 24

        val timeString = StringBuilder()
        if (days > 0) {
            timeString.append(days).append(" jours ")
        }
        if (hours > 0 || days > 0) {
            timeString.append(hours).append(" heures ")
        }
        if (minutes > 0 || hours > 0 || days > 0) {
            timeString.append(minutes).append(" minutes ")
        }
        timeString.append(seconds).append(" secondes")

        return timeString.toString()
    }

}