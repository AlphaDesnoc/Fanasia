package fr.alphadesnoc.fanarank.events

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.builders.ItemBuilder
import fr.alphadesnoc.fanacore.builders.gradient.GradientText
import fr.alphadesnoc.fanacore.builders.gradient.GradientTextBuilder
import fr.alphadesnoc.fanacore.utils.Rank
import org.bukkit.Bukkit
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Scoreboard


class PlayerListener : Listener
{

    @EventHandler
    fun onJoin(event: PlayerJoinEvent)
    {
        val player: Player = event.player
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: return
        val existingTeam = scoreboard.getEntryTeam(player.name)

        if (existingTeam != null) return

        FanaCore.fanaPlayerManager.editRank(player.name, Rank.MOUSSE)
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent)
    {
        val player: Player = event.player
        val rank: Rank? = FanaCore.fanaPlayerManager.getRank(player.name)
        val message: String = event.message
        lateinit var format: String
        lateinit var gradientText: GradientText
        if (rank!!.rPrefix == "Youtuber") {
            gradientText = GradientTextBuilder().text("[${rank.rName}] ").blur(0.2).addColor("#E81D1D").addColor("#333333").build()
            val gradientText2 = GradientTextBuilder().text(player.name).blur(0.2).addColor("#E81D1D").addColor("#333333").build()
            format = gradientText.renderText() + gradientText2.renderText() + ChatColor.DARK_GRAY + " : " + ChatColor.RESET + message
        } else {
            gradientText = GradientTextBuilder().text("[${rank.rName}] ${player.name}").blur(0.2).addColor(rank.rPrefix).build()
            format = gradientText.renderText() + ChatColor.DARK_GRAY + " : " + ChatColor.RESET + message
        }
        event.format = format
    }

}