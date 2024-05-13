package fr.alphadesnoc.fanablocks

import fr.alphadesnoc.fanacore.commands.CommandHandler
import fr.alphadesnoc.fanacore.events.EventHandler
import org.bukkit.plugin.java.JavaPlugin

class FanaBlocks : JavaPlugin()
{

    override fun onEnable() {
        EventHandler(plugin, "events")
        CommandHandler(plugin, "commands")
    }

    companion object
    {
        val plugin: FanaBlocks by lazy { getPlugin(FanaBlocks::class.java) }
    }

}