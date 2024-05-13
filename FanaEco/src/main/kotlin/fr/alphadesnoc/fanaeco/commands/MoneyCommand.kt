package fr.alphadesnoc.fanaeco.commands

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.builders.gradient.GradientTextBuilder
import fr.alphadesnoc.fanacore.commands.AbstractCommand
import fr.alphadesnoc.fanacore.data.player.FanaPlayerManager
import fr.alphadesnoc.fanacore.utils.Calc
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class MoneyCommand : AbstractCommand("money")
{

    private val fanaPlayerManager: FanaPlayerManager = FanaCore.fanaPlayerManager


    override fun handleCommand(player: Player, args: Array<out String>): Boolean {
        when (args.size) {
            0 -> showBalance(player, player.name)
            1 -> Bukkit.getPlayer(args[0])?.let {
                showBalance(player, it.name, it.name)
            } ?: player.sendMessage("Veuillez préciser un joueur valide !")
            2 -> if (args[1] == "reset") resetMoney(player, args[0])
            3 -> editMoney(player, args)
            else -> player.sendMessage("Commande invalide.")
        }

        return true
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String>
    {
        return when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }.toList()
            2 -> listOf("+", "-", "*", "/", "reset")
            else -> emptyList()
        }
    }

    private fun showBalance(player: Player, name: String, targetName: String = player.name)
    {
        val money = fanaPlayerManager.getMoney(name) ?: return player.sendMessage("Erreur lors de la récupération de l'argent.")
        player.sendMessage("Voici l'argent de $targetName: $money€")
    }

    private fun resetMoney(player: Player, targetName: String)
    {
        Bukkit.getPlayer(targetName)?.let {
            fanaPlayerManager.resetMoney(it.name)
            player.sendMessage("L'argent de $targetName a été réinitialisé.")
        } ?: player.sendMessage("Veuillez préciser un joueur valide !")
    }

    private fun editMoney(player: Player, args: Array<out String>)
    {
        val target = Bukkit.getPlayer(args[0])
        val operator = args[1].firstOrNull()
        val calc = operator?.let { Calc.getCalcByOpe(it) }
        val value = args[2].toDoubleOrNull()

        if (target == null || calc == null || value == null) {
            player.sendMessage("Veuillez vérifier les informations fournies : joueur, opérateur, et montant.")
            return
        }

        fanaPlayerManager.editMoney(target.name, value, calc)
        player.sendMessage(
            GradientTextBuilder()
                .text("Vous avez bien modifié l'argent de ${target.name} avec l'opération $operator qui a pour valeur $value !")
                .addColor("#ff0000") // Rouge
                .addColor("#00ff00") // Vert
                .addColor("#0000ff") // Bleu
                .blur(0.2)
                .build()
                .renderText()
        )
    }

}