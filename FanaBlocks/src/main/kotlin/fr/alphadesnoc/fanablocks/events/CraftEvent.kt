package fr.alphadesnoc.fanablocks.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

class CraftEvent : Listener
{

    @EventHandler
    fun onPrepareCraft(event: PrepareItemCraftEvent)
    {
        if (event.recipe != null && event.recipe!!.result.type == Material.SHULKER_BOX) {
            event.inventory.result = ItemStack(Material.AIR)
            val viewer = event.viewers.firstOrNull()
            viewer?.sendMessage("La fabrication de shulker box est désactivée sur ce serveur.")
        }
    }

}