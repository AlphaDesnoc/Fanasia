package fr.alphadesnoc.fanafight.runnable

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanafight.FanaFight
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class FightRunnable : BukkitRunnable()
{

    override fun run()
    {

        for (name in FanaFight.map.keys){
            var remainTime = FanaFight.map[name]
            val player = Bukkit.getPlayer(name)
            if (player == null) {
                FanaFight.map.remove(name)
                continue
            }
            if (remainTime!! > 0) {
                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent("${ChatColor.RED}Temps restant: $remainTime secondes")
                )
                FanaFight.map[name] = remainTime!!.minus(1)
            } else {
                player.sendMessage("Vous n'êtes plus en combat")
                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent("${ChatColor.GREEN}Vous pouvez à nouveau utiliser des commandes.")
                )
                FanaCore.fanaPlayerManager.editLastFightDate(player.name, 0)
                FanaFight.map.remove(name)
            }
        }

    }

}