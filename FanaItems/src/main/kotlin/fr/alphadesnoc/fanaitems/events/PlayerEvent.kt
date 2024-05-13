package fr.alphadesnoc.fanaitems.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PlayerEvent : Listener
{

    @EventHandler
    fun onItemPickup(event: EntityPickupItemEvent)
    {
        val item = event.item.itemStack
        if (item.type == Material.FIREWORK_ROCKET) {
            if(!item.hasItemMeta()){
                event.isCancelled = true
            }
            else {
                if(item.itemMeta!!.hasCustomModelData()) {
                    if (item.itemMeta!!.customModelData != 1) {
                        event.isCancelled = true
                    }
                }
                else {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onPlayerConsume(event: PlayerItemConsumeEvent)
    {
        val player = event.player
        val item = event.item

        if (item.type == Material.GOLDEN_APPLE) {
            event.isCancelled = true

            if (item.amount > 1) {
                item.amount -= 1
            } else {
                player.inventory.remove(item)
            }

            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1))
            player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 2, 0))
        }

        if (item.type == Material.ENCHANTED_GOLDEN_APPLE) {
            event.isCancelled = true

            if (item.amount > 1) {
                item.amount -= 1
            } else {
                player.inventory.remove(item)
            }

            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 1))
            player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 5, 1))
            player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 5, 0))
            player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 2, 3))
        }
    }
}