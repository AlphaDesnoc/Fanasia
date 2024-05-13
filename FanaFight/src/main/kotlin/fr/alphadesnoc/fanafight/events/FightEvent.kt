package fr.alphadesnoc.fanafight.events

import fr.alphadesnoc.fanafight.utils.FightPlayer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent

class FightEvent : Listener
{

    @EventHandler
    fun onJoin(event: PlayerJoinEvent)
    {
        FightPlayer.adjustPlayerAttackAttributes(event.player)
    }

    @EventHandler
    fun onEntityDamageEvent(event: EntityDamageByEntityEvent)
    {
        if (event.damager is Player) {
            val player = event.damager as Player
            val itemInHand = player.inventory.itemInMainHand.type

            event.damage = when (itemInHand) {
                Material.WOODEN_AXE -> 3.0
                Material.WOODEN_PICKAXE -> 2.0
                Material.WOODEN_SHOVEL -> 1.0
                Material.WOODEN_HOE -> 0.0
                Material.STONE_AXE -> 4.0
                Material.STONE_PICKAXE -> 3.0
                Material.STONE_SHOVEL -> 2.0
                Material.STONE_HOE -> 0.0
                Material.IRON_AXE -> 5.0
                Material.IRON_PICKAXE -> 4.0
                Material.IRON_SHOVEL -> 3.0
                Material.IRON_HOE -> 0.0
                Material.DIAMOND_AXE -> 6.0
                Material.DIAMOND_PICKAXE -> 5.0
                Material.DIAMOND_SHOVEL -> 4.0
                Material.DIAMOND_HOE -> 0.0
                Material.GOLDEN_AXE -> 3.0
                Material.GOLDEN_PICKAXE -> 2.0
                Material.GOLDEN_SHOVEL -> 1.0
                Material.GOLDEN_HOE -> 0.0
                Material.NETHERITE_AXE -> 7.0
                Material.NETHERITE_PICKAXE -> 6.0
                Material.NETHERITE_SHOVEL -> 5.0
                Material.NETHERITE_HOE -> 0.0
                else -> event.damage
            }
        }
    }

}