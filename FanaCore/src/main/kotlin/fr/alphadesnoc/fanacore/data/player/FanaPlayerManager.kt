package fr.alphadesnoc.fanacore.data.player

import fr.alphadesnoc.fanacore.builders.gradient.GradientTextBuilder
import fr.alphadesnoc.fanacore.data.CacheManager
import fr.alphadesnoc.fanacore.utils.Calc
import fr.alphadesnoc.fanacore.utils.Rank
import fr.alphadesnoc.fanacore.utils.punishment.Punishment
import fr.alphadesnoc.fanacore.utils.punishment.PunishmentDuration
import fr.alphadesnoc.fanacore.utils.punishment.PunishmentReason
import fr.alphadesnoc.fanacore.utils.punishment.PunishmentType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class FanaPlayerManager: CacheManager<String, FanaPlayer>(), Listener
{

    fun getMoney(name: String): Double?
    {
        return this.get(name)?.money
    }

    fun editMoney(name: String, amount: Double, calc: Calc)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.money = calc.execute(player.money, amount)

        this.put(name, player)
    }

    fun resetMoney(name: String)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.money = 0.0

        this.put(name, player)
    }

    fun getRank(name: String): Rank?
    {
        return this.get(name)?.rank
    }

    fun editRank(name: String, rank: Rank)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.rank = rank

        this.put(name, player)
        this.addTeamPlayer(name, rank)
    }

    fun listPunishment(name: String) : MutableList<Punishment> {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        return player.punishments
    }

    fun getPunishment(name: String, nbPunishment: Int) : Punishment
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        return player.punishments[nbPunishment]
    }

    fun hasWarn(name: String) : Boolean
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        return player.punishments.isNotEmpty()
    }

    fun addPunishment(name: String, type: PunishmentType, reason: PunishmentReason, duration: PunishmentDuration)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        val punishment = Punishment(UUID.randomUUID(), type, reason, SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date()), System.currentTimeMillis(), duration)
        player.punishments.add(punishment)
    }

    fun removePunishment(name: String, punishment: Punishment)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.punishments.remove(punishment)
    }

    fun hasPermission(name: String, rank: Rank): Boolean
    {
        //return getRank(uuid)!!.power >= rank.power
        return getRank(name)!!.power >= rank.power //TODO: A changer (seulement pour les test)
    }

    fun isBanned(name: String) : Boolean
    {
        return get(name)?.banned!!
    }

    fun isTempBanned(name: String) : Boolean
    {
        return get(name)?.tempBanned!!
    }

    fun getBannedLongDuration(name: String): Long
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        return LocalDateTime.parse(player.bannedDuration!!, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getBannedDuration(name: String): String
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        return player.bannedDuration!!
    }

    fun setBanned(name: String)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.banned = true
    }

    fun setTempBanned(name: String, bannedDate: String)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.tempBanned = true
        player.bannedDuration = bannedDate
    }

    fun setUnbanned(name: String)
    {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.banned = false
        player.tempBanned = false
        player.bannedDuration = null
    }

    fun addTeamPlayer(name: String, rank: Rank)
    {
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: return
        val teamName = rank.generateTeamNameWithPower()
        val team: Team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName).apply {
            val gradientText = if (rank.rPrefix == "Youtuber") {
                GradientTextBuilder().text(rank.rName).blur(0.2).addColor("#E81D1D").addColor("#000000").build()
            }
            else {
                GradientTextBuilder().text(rank.rName).blur(0.2).addColor(rank.rPrefix).build()
            }
            prefix = gradientText.renderText()
        }
        Bukkit.getPlayer(name)?.let { team.addEntry(it.name) }
    }

    fun getLastFightDate(name: String): Long? {
        return this.get(name)?.lastFightDate
    }

    fun editLastFightDate(name: String, date: Long) {
        val player = this.get(name) ?: FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf())
        player.lastFightDate = date
        this.put(name, player)
    }

    fun isInFight(name: String): Boolean {
        getLastFightDate(name)?.let {
            return System.currentTimeMillis() - it <= 30000
        }
        return false
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent)
    {
        val player: Player = event.player
        val name: String = player.name

        if (this.get(name) == null) {
            this.put(name, FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf()))
        }
    }

    @EventHandler
    fun onRestart(event: PluginEnableEvent)
    {
        val onlinePlayers = Bukkit.getOnlinePlayers()

        onlinePlayers.forEach { player ->
            val name: String = player.name

            if (this.get(name) == null) {
                this.put(name, FanaPlayer(0.0, Rank.MOUSSE, null, mutableListOf()))
            }
        }
    }

}