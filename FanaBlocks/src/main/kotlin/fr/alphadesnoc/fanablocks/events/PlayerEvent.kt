package fr.alphadesnoc.fanablocks.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class PlayerEvent : Listener
{

    @EventHandler
    fun onInteract(event: PlayerInteractEvent)
    {
        val action = event.action
        val blockClicked = event.clickedBlock

        if(action == Action.RIGHT_CLICK_BLOCK) {
            if (blockClicked!!.type == Material.SHULKER_BOX) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onItemPickup(event: EntityPickupItemEvent)
    {
        val item = event.item.itemStack
        if (item.type == Material.SHULKER_BOX || item.type == Material.SHULKER_SHELL) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent)
    {
        val block = event.block
        if (block.type == Material.SHULKER_BOX) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent)
    {
        val block = event.block
        if (block.type == Material.SHULKER_BOX) {
            event.isCancelled = true
        }
    }

}