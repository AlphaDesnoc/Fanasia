package fr.alphadesnoc.fanaitems

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import org.bukkit.plugin.java.JavaPlugin

class FanaItems : JavaPlugin()
{

    override fun onEnable() {
        EventHandler(plugin, "events")
        CommandHandler(plugin, "commands")
    }

    companion object
    {
        val plugin: FanaItems by lazy { getPlugin(FanaItems::class.java) }
    }

}