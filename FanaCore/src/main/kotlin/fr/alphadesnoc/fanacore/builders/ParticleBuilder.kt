package fr.alphadesnoc.fanacore.builders

import org.bukkit.Location
import org.bukkit.Particle

/**
 * A builder class for creating and spawning particles in a Bukkit environment. This class
 * allows for the fluent configuration and deployment of particle effects at specified locations
 * within the game world.
 *
 */
class ParticleBuilder(
    private var particle: Particle
)
{
    private var location: Location? = null
    private var count: Int = 1
    private var offsetX: Double = 0.0
    private var offsetY: Double = 0.0
    private var offsetZ: Double = 0.0
    private var extra: Double = 0.0

    /**
     * Sets the location where the particle effect will be spawned.
     *
     * @param location The [Location] at which the particle effect is to be spawned.
     * @return The current instance of [ParticleBuilder] for chaining method calls.
     */
    fun at(location: Location) = apply { this.location = location }

    /**
     * Sets the number of particles to spawn.
     *
     * @param count The number of particles to spawn.
     * @return The current instance of [ParticleBuilder] for chaining method calls.
     */
    fun count(count: Int) = apply { this.count = count }

    /**
     * Sets the offset for the particle effect, which controls the spread in each direction (x, y, z).
     *
     * @param offsetX The offset for the x-axis.
     * @param offsetY The offset for the y-axis.
     * @param offsetZ The offset for the z-axis.
     * @return The current instance of [ParticleBuilder] for chaining method calls.
     */
    fun offset(offsetX: Double, offsetY: Double, offsetZ: Double) = apply {
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.offsetZ = offsetZ
    }

    /**
     * Sets an additional parameter for the particle effect. The meaning of this parameter varies
     * between different particle types (e.g., speed, color, etc.).
     *
     * @param extra The extra parameter for the particle effect.
     * @return The current instance of [ParticleBuilder] for chaining method calls.
     */
    fun extra(extra: Double) = apply { this.extra = extra }

    /**
     * Spawns the configured particle effect at the specified location. If the location has not been
     * set prior to calling this method, an [IllegalStateException] is thrown.
     *
     * @throws IllegalStateException if the location is not set.
     */
    fun spawn()
    {
        location?.world?.spawnParticle(particle, location!!, count, offsetX, offsetY, offsetZ, extra)
            ?: throw IllegalStateException("Location is not set for ParticleBuilder")
    }
}