package fr.alphadesnoc.fanamod.commands

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.commands.AbstractCommand
import fr.alphadesnoc.fanacore.utils.Rank
import fr.alphadesnoc.fanamod.menus.MenuPlayer
import org.bukkit.entity.Player

class ModerationCommand : AbstractCommand("moderation", false)
{

    override fun handleCommand(player: Player, args: Array<out String>): Boolean
    {

        if (FanaCore.fanaPlayerManager.hasPermission(player.name, Rank.MODO)) {
            when (args.size) {
                0 -> handleMenu(player)
                else -> return false
            }
        }
        else {
            player.sendMessage("Vous n'avez pas la permission d'executer cette commande")
        }

        return true
    }

    private fun handleMenu(player: Player) {
        MenuPlayer.getModerationMenu(player)
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String>
    {
        return emptyList()
    }

}