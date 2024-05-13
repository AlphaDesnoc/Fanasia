package fr.alphadesnoc.fanacore

import fr.alphadesnoc.fanacore.data.player.FanaPlayerManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Scoreboard

class FanaCore : JavaPlugin() {

    override fun onLoad() {
        fanaPlayerManager = FanaPlayerManager()
    }

    override fun onEnable() {
        plugin.server.pluginManager.registerEvents(fanaPlayerManager, plugin)
    }

    override fun onDisable() {
        clearTeam()
    }

    companion object
    {
        lateinit var fanaPlayerManager: FanaPlayerManager
            private set
        val plugin: FanaCore by lazy { getPlugin(FanaCore::class.java) }
        val mutedPlayer = HashMap<String, Long>()
    }

    private fun clearTeam()
    {
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: return
        scoreboard.teams.forEach{
            it.unregister()
        }
    }

}