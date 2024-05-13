package fr.alphadesnoc.fanautils.clearlag

import fr.alphadesnoc.fanautils.FanaUtils
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.scheduler.BukkitRunnable

class ClearLag
{

    companion object {
        fun scheduleEntityClearTask(period: Long) {
            object : BukkitRunnable() {
                override fun run() {
                    clearEntities()
                }
            }.runTaskTimer(FanaUtils.plugin, 0, period * 20 * 60)
        }

        private fun clearEntities() {
            Bukkit.getWorlds().forEach { world ->
                world.entities.filter { it.type != EntityType.PLAYER }.forEach { entity ->
                    entity.remove()
                }
                world.players.forEach { player -> player.sendMessage("Toutes les entités ont été supprimé !")}
            }
        }
    }

}