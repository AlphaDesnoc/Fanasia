package fr.alphadesnoc.fanacore.commands

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

/**
 * An abstract class representing a custom command for a Bukkit plugin.
 * This class extends BukkitCommand and provides methods for executing commands and tab completing arguments.
 *
 * @property allowConsole Whether to allow the command to be executed from the console.
 * @constructor Creates an AbstractCommand with the given plugin instance, name, and console execution permission.
 */
abstract class AbstractCommand(
    name: String,
    private val allowConsole: Boolean = true
) : BukkitCommand(name) {

    /**
     * Executes the command.
     *
     * @param sender The CommandSender executing the command.
     * @param commandLabel The label of the command.
     * @param args The arguments passed to the command.
     * @return true if the command was executed successfully, false otherwise.
     */
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!allowConsole && sender is ConsoleCommandSender) {
            sender.sendMessage("This command cannot be executed from the console.")
            return false
        }

        if (sender is Player) {
            return handleCommand(sender, args)
        }

        return false
    }

    /**
     * Tab completes command arguments.
     *
     * @param sender The CommandSender executing the command.
     * @param alias The alias of the command.
     * @param args The arguments passed to the command.
     * @return A list of tab-completed strings.
     */
    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        return handleTabCompletion(if (sender is Player) sender else null, args)
    }

    /**
     * Handles the execution of the command.
     *
     * @param player The Player executing the command.
     * @param args The arguments passed to the command.
     * @return true if the command was executed successfully, false otherwise.
     */
    abstract fun handleCommand(player: Player, args: Array<out String>): Boolean

    /**
     * Handles tab completion for the command.
     *
     * @param player The Player executing the command, or null if the sender is not a player.
     * @param args The arguments passed to the command.
     * @return A list of tab-completed strings.
     */
    abstract fun handleTabCompletion(player: Player?, args: Array<out String>): List<String>
}
