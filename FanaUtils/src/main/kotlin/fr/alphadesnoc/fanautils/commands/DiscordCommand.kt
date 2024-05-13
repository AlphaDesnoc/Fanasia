package fr.alphadesnoc.fanautils.commands

import fr.alphadesnoc.fanacore.commands.AbstractCommand
import org.bukkit.entity.Player

class DiscordCommand : AbstractCommand("discord")
{

    override fun handleCommand(player: Player, args: Array<out String>): Boolean
    {
        player.sendMessage("Notre discord : https://discord.gg/d5aAFUaGeA")
        return true
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String>
    {
        return listOf()
    }

}