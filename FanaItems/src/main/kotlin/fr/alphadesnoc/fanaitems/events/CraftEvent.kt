package fr.alphadesnoc.fanaitems.events

import fr.alphadesnoc.fanacore.builders.ItemBuilder
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

class CraftEvent :Listener
{

    @EventHandler
    fun onPrepareCraft(event: PrepareItemCraftEvent)
    {
        if (event.recipe != null && event.recipe!!.result.type == Material.FIREWORK_ROCKET) {
            event.inventory.result = ItemStack(Material.AIR)
            val viewer = event.viewers.firstOrNull()
            viewer?.sendMessage("La fabrication de fusées est désactivée sur ce serveur.")
        }
    }

}