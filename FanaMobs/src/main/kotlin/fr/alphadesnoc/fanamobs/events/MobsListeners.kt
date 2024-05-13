package fr.alphadesnoc.fanamobs.events

import fr.alphadesnoc.fanacore.builders.gradient.GradientTextBuilder
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent


class MobsListeners: Listener
{

    private val lockedMobs: MutableSet<EntityType> = mutableSetOf(
        EntityType.ILLUSIONER,
        EntityType.VINDICATOR,
        EntityType.EVOKER,
        EntityType.EVOKER_FANGS,
        EntityType.PILLAGER,
        EntityType.DROWNED,
        EntityType.WARDEN,
        EntityType.WARDEN,
        EntityType.ENDER_DRAGON,
        EntityType.PHANTOM,
    )

    private val lockedMobsByEgg: MutableSet<EntityType> = mutableSetOf(
        EntityType.TURTLE,
        EntityType.SNIFFER,
    )

    @EventHandler
    fun onSpawnMob(event: CreatureSpawnEvent)
    {

        val entity = event.entity
        if (lockedMobs.contains(entity.type)
            && event.spawnReason != SpawnReason.SPAWNER_EGG
            && event.spawnReason == SpawnReason.BUILD_WITHER){
            event.isCancelled = true
        }

        if (lockedMobsByEgg.contains(entity.type) && event.spawnReason == SpawnReason.EGG) {
            event.isCancelled = true
        }

        if (event.spawnReason == SpawnReason.SPAWNER) {
            val nearbyEntities = entity.getNearbyEntities(10.0, 10.0, 10.0)
            nearbyEntities.forEach { nearbyEntity ->
                run {
                    if (nearbyEntity.type == entity.type) {
                        event.isCancelled = true
                        if(nearbyEntity.customName != null && nearbyEntity.customName!!.startsWith("§2x")) {
                            val stack = nearbyEntity.customName!!.drop(3).toInt()
                            nearbyEntity.customName = "§2x${stack+1}"
                        }
                        else {
                            nearbyEntity.customName = "§2x1"
                        }
                    }
                }
            }
        }

    }


    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity: Entity = event.entity
        if (event.cause == EntityDamageEvent.DamageCause.VOID) {
            entity.customName = " "
        }

        if(event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if(entity is Creature) {
                println(entity.customName + " - " + entity.health + "/" + entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
            }
        }
    }

    @EventHandler
    fun onMobDeath(event: EntityDeathEvent)
    {
        val entity = event.entity

        if (entity.customName != null) {
            entity.customName = null
            if(entity.customName!!.startsWith("§2x")) {
                val stack = entity.customName!!.drop(3).toInt()
                val newEntity = entity.location.world?.spawnEntity(entity.location, entity.type)
                if (newEntity != null && stack > 0 && (stack - 1) > 0) {
                    newEntity.customName = "§2x${stack-1}"
                }
            }
        }
    }

}