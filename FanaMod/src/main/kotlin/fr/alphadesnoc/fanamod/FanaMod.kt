package fr.alphadesnoc.fanamod

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import org.bukkit.plugin.java.JavaPlugin

class FanaMod : JavaPlugin()
{

    override fun onEnable() {
        EventHandler(plugin, "events")
        CommandHandler(plugin, "commands")
    }

    companion object
    {
        val plugin: FanaMod by lazy { getPlugin(FanaMod::class.java) }
    }

}