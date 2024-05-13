package fr.alphadesnoc.fanafight.utils

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import java.util.*

class FightPlayer {

    companion object
    {
        fun adjustPlayerAttackAttributes(player: Player) {
            val attackSpeedAttribute: AttributeInstance = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)!!
            val modifier = AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 100.0, AttributeModifier.Operation.ADD_NUMBER)

            attackSpeedAttribute.modifiers.forEach { attackSpeedAttribute.removeModifier(it) }

            attackSpeedAttribute.addModifier(modifier)
            player.saveData()
        }
    }

}