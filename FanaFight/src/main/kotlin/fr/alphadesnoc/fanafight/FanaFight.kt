package fr.alphadesnoc.fanafight

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import fr.alphadesnoc.fanafight.runnable.FightRunnable
import fr.alphadesnoc.fanafight.utils.FightPlayer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class FanaFight : JavaPlugin()
{

    override fun onEnable() {
        EventHandler(plugin, "events")
        CommandHandler(plugin, "commands")
        Bukkit.getOnlinePlayers().forEach { player ->  FightPlayer.adjustPlayerAttackAttributes(player)}
        FightRunnable().runTaskTimer(this, 0, 20L)
    }

    companion object
    {
        val plugin: FanaFight by lazy { getPlugin(FanaFight::class.java) }
        val map = HashMap<String, Int>()
        val blockingPlayers = HashSet<String>()
    }

}