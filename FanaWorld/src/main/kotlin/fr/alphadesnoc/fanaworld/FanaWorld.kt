package fr.alphadesnoc.fanaworld

import fr.alphadesnoc.fanacore.commands.CommandHandler
import org.bukkit.plugin.java.JavaPlugin

class FanaWorld : JavaPlugin()
{

    override fun onEnable() {
        CommandHandler(plugin, "commands")
    }

    companion object
    {
        val plugin: FanaWorld by lazy { getPlugin(FanaWorld::class.java) }
    }

}