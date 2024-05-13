package fr.alphadesnoc.fanamobs

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import org.bukkit.plugin.java.JavaPlugin

class FanaMobs : JavaPlugin()
{

    override fun onEnable() {
        EventHandler(plugin, "events")
        CommandHandler(plugin, "commands")
    }

    companion object
    {
        val plugin: FanaMobs by lazy { getPlugin(FanaMobs::class.java) }
    }

}