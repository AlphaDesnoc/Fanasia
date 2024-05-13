package fr.alphadesnoc.fanamod.events

import fr.alphadesnoc.fanacore.FanaCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerEvent : Listener
{

    @EventHandler
    fun onJoin(event: PlayerJoinEvent)
    {
        val player = event.player
        if(FanaCore.fanaPlayerManager.isBanned(player.name))
        {
            player.kickPlayer("Vous avez été banni sans durée définie !")
        }
        if(FanaCore.fanaPlayerManager.isTempBanned(player.name)) {
            if (System.currentTimeMillis() <= FanaCore.fanaPlayerManager.getBannedLongDuration(player.name)) {
                player.kickPlayer("Vous avez été banni jusqu'au : ${FanaCore.fanaPlayerManager.getBannedDuration(player.name)} !")
            }
            else {
                FanaCore.fanaPlayerManager.setUnbanned(player.name)
            }
        }
    }

}