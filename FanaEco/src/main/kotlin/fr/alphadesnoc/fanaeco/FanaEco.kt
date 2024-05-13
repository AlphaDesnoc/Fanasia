package fr.alphadesnoc.fanaeco

import fr.alphadesnoc.fanacore.commands.CommandHandler
import org.bukkit.plugin.java.JavaPlugin

class FanaEco : JavaPlugin()
{

    override fun onEnable()
    {
        CommandHandler(plugin, "commands")
    }

    companion object
    {
        val plugin: FanaEco by lazy { getPlugin(FanaEco::class.java) }
    }

}