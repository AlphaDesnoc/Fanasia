package fr.alphadesnoc.fanaenchants

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import org.bukkit.plugin.java.JavaPlugin

class FanaEnchants : JavaPlugin()
{

    override fun onEnable() {
        EventHandler(plugin, "events")
        CommandHandler(plugin, "commands")
    }

    companion object
    {
        val plugin: FanaEnchants by lazy { getPlugin(FanaEnchants::class.java) }
    }

}