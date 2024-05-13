package fr.alphadesnoc.fanafight.events

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.utils.punishment.PunishmentDuration
import fr.alphadesnoc.fanacore.utils.punishment.PunishmentReason
import fr.alphadesnoc.fanacore.utils.punishment.PunishmentType
import fr.alphadesnoc.fanacore.utils.punishment.WarnReason
import fr.alphadesnoc.fanafight.FanaFight
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.math.min

class FightCooldownEvent : Listener
{

    @EventHandler
    fun onPlayerBlock(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player
            if (player.inventory.itemInMainHand.type == Material.SHIELD) {
                // Vérifier si le joueur est en combat
                if (FanaCore.fanaPlayerManager.isInFight(player.name)) {
                    FanaFight.blockingPlayers.add(player.name)
                    val currentTime = System.currentTimeMillis()
                    FanaCore.fanaPlayerManager.editLastFightDate(player.name, currentTime)

                    player.world.playSound(player.location, Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f)
                }
            }
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player || event.damager !is Player) return

        val damager = event.damager as Player
        val victim = event.entity as Player

        event.damage = event.damage

        applyCustomKnockback(damager, victim)

        updateCombatState(damager, victim)

        if (FanaFight.blockingPlayers.contains(victim.name)) {
            event.damage *= 0.5

            victim.sendMessage("Vous avez bloqué une partie de l'attaque !")

            FanaFight.blockingPlayers.remove(victim.name)

            victim.world.playSound(victim.location, Sound.BLOCK_ANVIL_HIT, 1.0f, 0.8f)
        }
    }

    private fun applyCustomKnockback(damager: Player, victim: Player) {
        val weapon = damager.inventory.itemInMainHand
        val knockbackLevel = weapon.getEnchantmentLevel(Enchantment.KNOCKBACK)
        val direction = victim.location.toVector().subtract(damager.location.toVector()).normalize()
        val baseVerticalComponent = 0.35
        val intensity = 0.5 + 0.25 * knockbackLevel

        val currentVelocity = victim.velocity
        val newY = min(currentVelocity.y + baseVerticalComponent, 0.5)
        direction.setY(newY)
        direction.multiply(intensity)

        victim.velocity = direction
    }

    private fun updateCombatState(damager: Player, victim: Player) {
        if (!FanaCore.fanaPlayerManager.isInFight(damager.name)) {
            damager.sendMessage("Vous êtes en combat !")
        }
        if (!FanaCore.fanaPlayerManager.isInFight(victim.name)) {
            victim.sendMessage("Vous êtes en combat !")
        }

        val currentTime = System.currentTimeMillis()
        FanaFight.map[damager.name] = 30
        FanaCore.fanaPlayerManager.editLastFightDate(damager.name, currentTime)
        FanaFight.map[victim.name] = 30
        FanaCore.fanaPlayerManager.editLastFightDate(victim.name, currentTime)
    }



    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            FanaFight.map.remove(player.name)
            FanaCore.fanaPlayerManager.editLastFightDate(player.name, 0)
            player.sendMessage("Vous n'êtes plus en combat")
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        if (FanaCore.fanaPlayerManager.isInFight(player.name)) {
            player.sendMessage("Vous ne pouvez pas utiliser de commandes pendant 30 secondes après être entré en combat.")
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        if (FanaCore.fanaPlayerManager.isInFight(player.name)) {
            FanaFight.map.remove(player.name)
            FanaCore.fanaPlayerManager.editLastFightDate(player.name, 0);
            FanaCore.fanaPlayerManager.addPunishment(player.name, PunishmentType.WARN, PunishmentReason.Warn(WarnReason.DECO_COMBAT.desc), PunishmentDuration.NULL)
        }
    }

}