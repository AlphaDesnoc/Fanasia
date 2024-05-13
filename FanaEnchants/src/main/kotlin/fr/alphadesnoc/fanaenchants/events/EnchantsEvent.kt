package fr.alphadesnoc.fanaenchants.events

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.entity.VillagerAcquireTradeEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe


class EnchantsEvent : Listener
{

    @EventHandler
    fun onVillagerAcquireTrade(event: VillagerAcquireTradeEvent)
    {

        val recipe: MerchantRecipe = event.recipe
        val result: ItemStack = recipe.result


        val hasUndesiredEnchant = result.containsEnchantment(Enchantment.MENDING) ||
                result.containsEnchantment(Enchantment.FROST_WALKER)

        if (hasUndesiredEnchant) {
            event.isCancelled = true
        }

    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val entity: Entity = event.rightClicked
        if (entity is Villager) {
            val newRecipes: MutableList<MerchantRecipe> = ArrayList()
            for (recipe in entity.recipes) {
                val result = recipe.result
                if (!result.containsEnchantment(Enchantment.MENDING) &&
                    !result.containsEnchantment(Enchantment.FROST_WALKER)
                ) {
                    newRecipes.add(recipe)
                }
            }
            entity.recipes = newRecipes
        }
    }

    @EventHandler
    fun onEnchantItem(event: EnchantItemEvent) {
        val enchantsToAdd = HashMap(event.enchantsToAdd)
        var hasChanged = false

        if (enchantsToAdd.containsKey(Enchantment.MENDING)) {
            enchantsToAdd.remove(Enchantment.MENDING)
            hasChanged = true
        }
        if (enchantsToAdd.containsKey(Enchantment.FROST_WALKER)) {
            enchantsToAdd.remove(Enchantment.FROST_WALKER)
            hasChanged = true
        }

        if (hasChanged) {
            event.enchantsToAdd.clear()
            event.enchantsToAdd.putAll(enchantsToAdd)
        }
    }

}