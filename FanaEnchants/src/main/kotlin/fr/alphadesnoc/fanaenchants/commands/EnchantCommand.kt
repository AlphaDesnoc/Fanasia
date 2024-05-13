package fr.alphadesnoc.fanaenchants.commands

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.commands.AbstractCommand
import fr.alphadesnoc.fanacore.utils.Rank
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class EnchantCommand : AbstractCommand("fanaenchant")
{

    override fun handleCommand(player: Player, args: Array<out String>): Boolean {
        val item: ItemStack = player.inventory.itemInMainHand

        if (FanaCore.fanaPlayerManager.hasPermission(player.name, Rank.FONDA)) {
            if(item.type != Material.AIR) {

                when(args.size) {
                    1 -> if (args[0] == "purify") purifyItem(item)
                    2 -> {
                        if(args[0] == "remove") removeEnchant(item, args[1])
                        else if (args[0] == "mighty") mightyEnchant(item, args[1])
                    }
                    3 -> if(args[0] == "add") addEnchant(item, args)
                    else -> player.sendMessage("Commande invalide")

                }

            }
            else {
                player.sendMessage("Vous n'avez pas d'item dans votre main !")
            }
        }
        else {
            player.sendMessage("Vous n'avez pas la permission")
        }

        return true
    }

    private fun purifyItem(item: ItemStack) {
        item.removeEnchantments()
    }

    private fun mightyEnchant(item: ItemStack, s: String) {
        Registry.ENCHANTMENT.forEach {
            if (it.canEnchantItem(item)) {
                item.addUnsafeEnchantment(it, s.toInt())
            }
        }
    }

    private fun addEnchant(item: ItemStack, args: Array<out String>) {
        val enchantName = args[1]
        val enchantLevel = args[2].toInt()

        NamespacedKey.fromString(enchantName)?.let {
            Registry.ENCHANTMENT.get(it)?.let { it1 -> item.addUnsafeEnchantment(it1, enchantLevel) }
        }
    }

    private fun removeEnchant(item: ItemStack, s: String) {
        NamespacedKey.fromString(s)?.let {
            Registry.ENCHANTMENT.get(it)?.let { it1 -> item.removeEnchantment(it1) }
        }
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String> {
        return when (args.size) {
            1 -> listOf("remove", "add", "mighty", "purify")
            2 -> {
                if(args[0] == "mighty") listOf("1", "2", "3", "4", "5")
                else Registry.ENCHANTMENT.toList().map { it.key.key }
            }
            else -> emptyList()
        }

    }

}