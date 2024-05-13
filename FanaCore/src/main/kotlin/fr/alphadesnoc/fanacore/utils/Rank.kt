package fr.alphadesnoc.fanacore.utils

import fr.alphadesnoc.fanacore.builders.gradient.GradientTextBuilder
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

enum class Rank(
    val rName: String,
    val rPrefix: String,
    val power: Int
)
{

    FONDA("Dirigieant ", "#FF5733", 1),
    DEV("Developpeur ", "#33E3FF", 2),
    SYS_ADMIN("Sys-Admin ", "#D64DD4", 3),
    RESP_STAFF("Responsable ", "#D6BD4D", 4),
    MODO("Moderateur ", "#0D6A36", 5),
    HELPER("Helper ", "#12B85B", 6),
    YOUTUBER("Youtubeur ", "Youtuber", 7),
    PARTENAIRE("Partenaire ", "#A553D3", 8),
    MARECHAL("Marechal ", "#7922F0", 9),
    AMIRAL("Amiral ", "#7263EA", 10),
    CAPITAINE("Capitaine ", "#6390EA", 11),
    LIEUTENANT("Lieutenant ", "#63ABEA", 12),
    MAJOR("Major ", "#6AACEE", 13),
    MAITRE("Maitre ", "#6ABAEE", 14),
    MATELOT("Matelot ", "#41C2EB", 15),
    MOUSSE("Mousse ", "#5E6264", 16),
    NULL("ERREUR", "#FF0000", 0)
    ;

    fun generateTeamNameWithPower(): String
    {
        return "\u2063".repeat(power) + "${power}_${this.rName}"
    }

    companion object
    {
        infix fun getRankByPower(power: Int): Rank?
        {
            return entries.find { it.power == power }
        }

        infix fun getRankByName(name: String): Rank
        {
            for (rank in entries) {
                if (rank.rName.replace(" ", "") == name) return rank
            }
            return NULL
        }

        fun loadAllRanks() {
            entries.forEach { rank: Rank ->
                run {
                    val scoreboard: Scoreboard? = Bukkit.getScoreboardManager()?.mainScoreboard
                    val teamName = rank.generateTeamNameWithPower()
                    if(scoreboard!!.getTeam(teamName) == null){
                        scoreboard.registerNewTeam(teamName).apply {
                            val gradientText = if (rank.rPrefix == "Youtuber") {
                                GradientTextBuilder().text(rank.rName).blur(0.2).addColor("#E81D1D").addColor("#000000").build()
                            }
                            else {
                                GradientTextBuilder().text(rank.rName).blur(0.2).addColor(rank.rPrefix).build()
                            }
                            prefix = gradientText.renderText()
                            setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                        }
                    }
                }
            }
        }
    }
}