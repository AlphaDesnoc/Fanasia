package fr.alphadesnoc.fanarank.commands

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.commands.AbstractCommand
import fr.alphadesnoc.fanacore.data.player.FanaPlayerManager
import fr.alphadesnoc.fanacore.utils.Rank
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class RankCommand : AbstractCommand("rank")
{

    private val fanaPlayerManager: FanaPlayerManager = FanaCore.fanaPlayerManager

    override fun handleCommand(player: Player, args: Array<out String>): Boolean
    {
        if (fanaPlayerManager.hasPermission(player.name, Rank.FONDA)) {
            when(args.size) {
                2 -> Bukkit.getPlayer(args[0])?.let {
                    editRank(it, args[1])
                } ?: player.sendMessage("Veuillez préciser un joueur valide !")
                else -> player.sendMessage("Commande invalide.")
            }
        }
        else {
            player.sendMessage("Vous n'avez pas la permission pour executer cette commande")
        }

        return true
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String>
    {
        return when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }.toList()
            2 -> Rank.entries.map { it.rName }.toList()
            else -> emptyList()
        }
    }

    private fun editRank(player: Player, rankName: String)
    {
        val rank = Rank.getRankByName(rankName)
        fanaPlayerManager.editRank(player.name, rank)
    }

}