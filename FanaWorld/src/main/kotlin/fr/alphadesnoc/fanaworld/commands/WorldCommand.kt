package fr.alphadesnoc.fanaworld.commands

import fr.alphadesnoc.fanacore.commands.AbstractCommand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import java.util.function.Consumer


class WorldCommand : AbstractCommand("world")
{
    override fun handleCommand(player: Player, args: Array<out String>): Boolean {
        Bukkit.createWorld(WorldCreator(args[0]))
        val world: World? = Bukkit.getWorld(args[0])
        player.teleport(world!!.spawnLocation)
        return true
    }

    override fun handleTabCompletion(player: Player?, args: Array<out String>): List<String> {
        val names: MutableList<String> = ArrayList()
        Bukkit.getWorlds().forEach(Consumer { world: World ->
            names.add(
                world.name
            )
        })
        return names
    }


}