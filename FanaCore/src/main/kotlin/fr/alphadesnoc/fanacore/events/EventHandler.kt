package fr.alphadesnoc.fanacore.events

import com.google.common.reflect.ClassPath
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException

/**
 * A class for handling the registration of Bukkit events from a specified package path.
 *
 * @param plugin The JavaPlugin instance owning this command handler.
 * @param path The package path to scan for command classes.
 */
class EventHandler(
    plugin: JavaPlugin,
    path: String
)
{
    init
    {
        val pm = plugin.server.pluginManager

        try {
            val classPath = ClassPath.from(plugin::class.java.classLoader)
            classPath.getTopLevelClassesRecursive("${plugin.javaClass.`package`.name}.$path").forEach { classInfo ->
                try {
                    val c = Class.forName(classInfo.name)
                    val obj = c.getDeclaredConstructor().newInstance()

                    if (obj is Listener) {
                        pm.registerEvents(obj, plugin)
                    }
                } catch (e: Exception) {
                    plugin.logger.severe("Error while registering the events: ${e.message}")
                }
            }
        } catch (e: IOException) {
            plugin.logger.severe("Error while searching the ClassPath: ${e.message}")
        }
    }
}