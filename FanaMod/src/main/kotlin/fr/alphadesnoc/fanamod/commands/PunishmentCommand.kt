package fr.alphadesnoc.fanamod.commands

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.commands.AbstractCommand
import fr.alphadesnoc.fanacore.utils.Rank
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PunishmentCommand : AbstractCommand("punishment")
{

    override fun handleCommand(player: Player, args: Array<out String>): Boolean
    {
        if (FanaCore.fanaPlayerManager.hasPermission(player.name, Rank.MODO)) {
            when(args.size)
            {
                2 -> {
                    if (args[0] == "list") {
                        val targetName = args[1]
                        if (FanaCore.fanaPlayerManager.hasWarn(targetName)) {
                            val stringBuilder = StringBuilder()
                            stringBuilder.append("---------------------------\n")
                            stringBuilder.append("Liste des sanctions de $targetName : \n")
                            FanaCore.fanaPlayerManager.listPunishment(targetName).forEach{punishment ->
                                run {
                                    stringBuilder.append("${punishment.reason} le ${punishment.date}\n")
                                }
                            }
                            stringBuilder.append("---------------------------\n")
                            player.sendMessage(stringBuilder.toString())
                        }
                        else {
                            player.sendMessage("Ce joueur n'a pas de sanction")
                        }
                    }
                    else if (args[0] == "clear") {
                        val targetName = args[1]
                        if (FanaCore.fanaPlayerManager.hasWarn(targetName)) {
                            FanaCore.fanaPlayerManager.listPunishment(targetName).clear()
                            player.sendMessage("Les avertissements de $targetName ont été clear")
                        }
                        else {
                            player.sendMessage("Ce joueur n'a pas de sanction")
                        }
                    }
                }
            }
            return true
        }
        else {
            player.sendMessage("Vous n'avez pas la permission d'utiliser cette commande !")
            return false
        }
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String>
    {
        return when(args.size) {
            1 -> listOf("list", "clear")
            2 -> Bukkit.getOnlinePlayers().map { it.name }.toList()
            else -> emptyList()
        }
    }

}