package fr.alphadesnoc.fanarank

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import fr.alphadesnoc.fanacore.utils.Rank
import org.bukkit.plugin.java.JavaPlugin

class FanaRank : JavaPlugin()
{

    override fun onEnable() {
        CommandHandler(plugin, "commands")
        EventHandler(plugin, "events")
        Rank.loadAllRanks()
    }

    companion object
    {
        val plugin: FanaRank by lazy { getPlugin(FanaRank::class.java) }
    }

}