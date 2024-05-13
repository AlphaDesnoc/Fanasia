package fr.alphadesnoc.fanautils

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import fr.alphadesnoc.fanautils.clearlag.ClearLag
import org.bukkit.plugin.java.JavaPlugin

class FanaUtils : JavaPlugin()
{

    override fun onEnable() {
        EventHandler(plugin, "events")
        CommandHandler(plugin, "commands")
        ClearLag.scheduleEntityClearTask(15)
    }

    companion object
    {
        val plugin: FanaUtils by lazy { getPlugin(FanaUtils::class.java) }
    }

}