package fr.alphadesnoc.fanacore.data

import java.util.concurrent.ConcurrentHashMap

abstract class CacheManager<K, V>
{
    private val cache = ConcurrentHashMap<K, CacheItem<V>>()

    fun put(key: K, value: V)
    {
        cache[key] = CacheItem(value)
    }

    fun get(key: K): V?
    {
        val item = cache[key]
        return if (item != null) {
            item.value
        } else {
            cache.remove(key)
            null
        }
    }

    fun remove(key: K): V?
    {
        return cache.remove(key)?.value
    }

    fun clear()
    {
        cache.clear()
    }

    private data class CacheItem<V>(val value: V)
}