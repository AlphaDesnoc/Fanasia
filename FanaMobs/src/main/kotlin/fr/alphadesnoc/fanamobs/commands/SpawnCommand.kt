package fr.alphadesnoc.fanamobs.commands

import fr.alphadesnoc.fanacore.builders.MobBuilder
import fr.alphadesnoc.fanacore.commands.AbstractCommand
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SpawnCommand: AbstractCommand("mob") {
    override fun handleCommand(player: Player, args: Array<out String>): Boolean {
        val creature = MobBuilder(player, EntityType.WARDEN)
            .name("Test")
            .attack(1.0)
            .helmet(ItemStack(Material.DIAMOND_HELMET))
            .health(1000.0)
            .glowing(true)
            .build()

        player.sendMessage(creature.customName + " a spawn avec " + creature.health + " points de vie")
        return true
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String> {
        TODO("Not yet implemented")
    }
}