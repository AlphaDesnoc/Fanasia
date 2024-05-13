package fr.alphadesnoc.fanacore.builders

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

class MobBuilder(player: Player, type: EntityType) {

    private val creature: Creature = player.location.world!!.spawnEntity(player.location, type) as Creature

    fun health(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = value
        creature.health = value
        return this
    }

    fun attack(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)!!.baseValue = value
        return this
    }

    fun attackSpeed(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_ATTACK_SPEED)!!.baseValue = value
        return this
    }

    fun speed(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.baseValue = value
        return this
    }

    fun kbResist(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.baseValue = value
        return this
    }

    fun kbAttack(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)!!.baseValue = value
        return this
    }

    fun absorbtion(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_MAX_ABSORPTION)!!.baseValue = value
        creature.absorptionAmount = value
        return this
    }

    fun armor(value: Double): MobBuilder {
        creature.getAttribute(Attribute.GENERIC_ARMOR)!!.baseValue = value
        return this
    }

    fun name(value: String): MobBuilder {
        creature.customName = value
        return this
    }

    fun helmet(item: ItemStack): MobBuilder {
        creature.equipment!!.helmet = item
        return this
    }

    fun chestplate(item: ItemStack): MobBuilder {
        creature.equipment!!.chestplate = item
        return this
    }

    fun leggings(item: ItemStack): MobBuilder {
        creature.equipment!!.leggings = item
        return this
    }

    fun boots(item: ItemStack): MobBuilder {
        creature.equipment!!.boots = item
        return this
    }

    fun invisible(value: Boolean): MobBuilder {
        creature.isInvisible = value
        return this
    }

    fun invulnerable(value: Boolean): MobBuilder {
        creature.isInvulnerable = value
        return this
    }

    fun glowing(value: Boolean): MobBuilder {
        creature.isGlowing = value
        return this
    }

    fun potionEffect(potionEffect: PotionEffect): MobBuilder {
        creature.addPotionEffect(potionEffect)
        return this
    }

    fun build(): Creature {
        return creature
    }

}