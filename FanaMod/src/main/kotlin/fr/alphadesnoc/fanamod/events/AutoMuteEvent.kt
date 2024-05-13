package fr.alphadesnoc.fanamod.events

import fr.alphadesnoc.fanamod.FanaMod
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.regex.Pattern


class AutoMuteEvent : Listener
{

    private val offenseCount = HashMap<String, Int>()
    private val muteTasks = HashMap<String, BukkitTask>()
    private val bannedWords =
        arrayOf("pute", "fdp", "salope", "encule", "merde", "enfoiré", "chier", "con", "conne", "putin")

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val playerName = event.player.name
        val message = event.message.lowercase(Locale.getDefault())
        for (word in bannedWords) {
            if (Pattern.compile("\\b$word\\b").matcher(message)
                    .find()
            ) { // Enhance pattern for approximations if needed.
                val count = offenseCount.getOrDefault(playerName, 0) + 1
                offenseCount[playerName] = count

                val muteDuration = getMuteDuration(count)
                mutePlayer(playerName, muteDuration)
                event.player.sendMessage("You have been muted for using inappropriate language.")
                event.isCancelled = true
                break
            }
        }
    }

    private fun mutePlayer(playerName: String, duration: Long) {
        if (muteTasks.containsKey(playerName)) {
            muteTasks[playerName]!!.cancel()
        }
        val task: BukkitTask = Bukkit.getScheduler().runTaskLater(
            FanaMod.plugin,
            Runnable { unmutePlayer(playerName) }, duration * 20L * 60
        )
        muteTasks[playerName] = task
        Bukkit.getServer()
            .broadcastMessage(Bukkit.getPlayer(playerName)!!.name + " has been muted for " + duration + " minutes.")
    }

    private fun unmutePlayer(playerName: String) {
        muteTasks.remove(playerName)
    }


    private fun getMuteDuration(offenses: Int): Long {
        return when (offenses) {
            1 -> 15
            2 -> 30
            3 -> 60
            4 -> 180
            5 -> 360
            6 -> 720
            7 -> 1440
            8 -> 4320
            9 -> 10080
            else -> 10080
        }
    }

}