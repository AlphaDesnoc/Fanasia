package fr.alphadesnoc.fanacore.commands

import com.google.common.reflect.ClassPath
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.TabCompleter
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException

/**
 * A class for handling the registration of Bukkit commands and tab completers from a specified package path.
 *
 * @param plugin The JavaPlugin instance owning this command handler.
 * @param path The package path to scan for command classes.
 */
class CommandHandler(
    plugin: JavaPlugin,
    path: String
)
{
    init
    {
        try {
            val classPath = ClassPath.from(plugin::class.java.classLoader)

            classPath.getTopLevelClassesRecursive("${plugin.javaClass.`package`.name}.$path").forEach { classInfo ->
                try {
                    val clazz = classInfo.load()

                    if (BukkitCommand::class.java.isAssignableFrom(clazz)) {
                        val commandInstance = clazz.getDeclaredConstructor().newInstance()

                        if (commandInstance is Command) {
                            val commandMap = Bukkit.getServer().javaClass.getDeclaredMethod("getCommandMap")
                                .invoke(Bukkit.getServer())
                            commandMap.javaClass.getMethod("register", String::class.java, Command::class.java)
                                .invoke(commandMap, plugin.description.name, commandInstance)

                            if (commandInstance is TabCompleter) {
                                plugin.getCommand(commandInstance.name)?.tabCompleter = commandInstance
                            }
                        }
                    }
                } catch (e: Exception) {
                    plugin.logger.severe("Error while registering Commands: ${e.message}")
                }
            }
        } catch (e: IOException) {
            plugin.logger.severe("Error while searching the ClassPath for the Commands: ${e.message}")
        }
    }
}