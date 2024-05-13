package fr.alphadesnoc.fanacore.builders

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta

/**
 * A utility class for building and modifying ItemStack objects.
 *
 * @property itemStack The ItemStack object currently being built or modified.
 * @constructor Creates an [ItemBuilder] from a [Material] type.
 * @constructor Creates an [ItemBuilder] from an existing [ItemStack].
 * @constructor Creates an [ItemBuilder] from a [Material] type and quantity.
 * @constructor Creates an [ItemBuilder] from a [Material] type, quantity, and durability.
 */
class ItemBuilder
{
    private val itemStack: ItemStack

    constructor(material: Material)
    {
        this.itemStack = ItemStack(material, 1)
    }

    constructor(itemStack: ItemStack)
    {
        this.itemStack = itemStack
    }

    constructor(material: Material, amount: Int)
    {
        this.itemStack = ItemStack(material, amount)
    }

    constructor(material: Material, amount: Int, durability: Byte)
    {
        this.itemStack = ItemStack(material, amount, durability.toShort())
    }

    /**
     * Creates a copy of the current ItemStack object.
     *
     * @return a new instance of [ItemBuilder] with a copy of the ItemStack object.
     */

    fun clone(): ItemBuilder
    {
        return ItemBuilder(itemStack.clone())
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability The new durability value to set.
     * @return The ItemBuilder instance with the updated durability.
     */
    fun setDurability(durability: Int): ItemBuilder {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta is Damageable) {
            itemMeta.damage = durability
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Sets the display name of the ItemStack object.
     *
     * @param displayName The display name to set.
     * @return this instance of [ItemBuilder].
     */
    fun setDisplayName(displayName: String): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName)
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Adds an unsafe enchantment to the ItemStack object.
     *
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     * @return this instance of [ItemBuilder].
     */
    fun addEnchantment(enchantment: Enchantment, level: Int): ItemBuilder
    {
        itemStack.addUnsafeEnchantment(enchantment, level)
        return this
    }

    /**
     * Removes an enchantment from the ItemStack object.
     *
     * @param enchantment The enchantment to remove.
     * @return this instance of [ItemBuilder].
     */
    fun removeEnchantment(enchantment: Enchantment): ItemBuilder
    {
        itemStack.removeEnchantment(enchantment)
        return this
    }

    /**
     * Sets the owner of the head for the ItemStack object.
     *
     * @param craftPlayer The player whose profile is used as the head owner.
     * @return this instance of [ItemBuilder].
     */
    fun setHeadOwner(craftPlayer: CraftPlayer): ItemBuilder
    {
        val skullMeta: SkullMeta? = itemStack.itemMeta as? SkullMeta
        if (skullMeta != null) {
            skullMeta.ownerProfile = craftPlayer.playerProfile
            itemStack.itemMeta = skullMeta
        }
        return this
    }

    /**
     * Makes the ItemStack object unbreakable.
     *
     * @return this instance of [ItemBuilder].
     */
    fun setUnbreakable(): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            itemMeta.isUnbreakable = true
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Sets the description of the ItemStack object.
     *
     * @param lore The list of description lines to set.
     * @return this instance of [ItemBuilder].
     */
    fun setLore(vararg lore: String): ItemBuilder
    {
        val im: ItemMeta? = itemStack.itemMeta
        im?.lore = lore.toList()
        itemStack.itemMeta = im
        return this
    }

    /**
     * Sets the description of the ItemStack object.
     *
     * @param lore The list of description lines to set.
     * @return this instance of [ItemBuilder].
     */
    fun setLore(lore: List<String>): ItemBuilder
    {
        val im: ItemMeta? = itemStack.itemMeta
        im?.lore = lore
        itemStack.itemMeta = im
        return this
    }

    /**
     * Removes a description line from the ItemStack object.
     *
     * @param line The description line to remove.
     * @return this instance of [ItemBuilder].
     */
    fun removeLoreLine(line: String): ItemBuilder
    {
        val im: ItemMeta? = itemStack.itemMeta
        val lore = im?.lore ?: return this
        if (!lore.contains(line)) return this
        lore.remove(line)
        im.lore = lore
        itemStack.itemMeta = im
        return this
    }

    /**
     * Removes a description line from the ItemStack object.
     *
     * @param index The index of the description line to remove.
     * @return this instance of [ItemBuilder].
     */
    fun removeLoreLine(index: Int): ItemBuilder
    {
        val im: ItemMeta? = itemStack.itemMeta
        val lore = im?.lore ?: return this
        if (index < 0 || index >= lore.size) return this
        lore.removeAt(index)
        im.lore = lore
        itemStack.itemMeta = im
        return this
    }


    /**
     * Adds a description line to the ItemStack object.
     *
     * @param line The description line to add.
     * @return this instance of [ItemBuilder].
     */
    fun addLoreLine(line: String): ItemBuilder
    {
        val im: ItemMeta? = itemStack.itemMeta
        val lore = im?.lore?.toMutableList() ?: mutableListOf()
        lore.add(line)
        if (im != null) {
            im.lore = lore
        }
        itemStack.itemMeta = im
        return this
    }

    /**
     * Adds a description line at a specific position in the ItemStack object.
     *
     * @param line The description line to add.
     * @param pos The position at which to add the description line.
     * @return this instance of [ItemBuilder].
     */
    fun addLoreLine(line: String, pos: Int): ItemBuilder
    {
        val im: ItemMeta? = itemStack.itemMeta
        val lore = im?.lore?.toMutableList() ?: mutableListOf()
        if (pos >= 0 && pos <= lore.size) {
            lore.add(pos, line)
            if (im != null) {
                im.lore = lore
            }
            itemStack.itemMeta = im
        }
        return this
    }

    /**
     * Sets the color of leather armor for the ItemStack object (if applicable).
     *
     * @param color The color to set.
     * @return this instance of [ItemBuilder].
     */
    fun setLeatherArmorColor(color: Color): ItemBuilder
    {
        if (itemStack.itemMeta is LeatherArmorMeta) {
            val im = itemStack.itemMeta as LeatherArmorMeta
            im.setColor(color)
            itemStack.itemMeta = im
        }
        return this
    }

    /**
     * Sets an attribute modifier for the ItemStack object.
     *
     * @param attribute The attribute to modify.
     * @param modifier The attribute modifier to apply.
     * @return this instance of [ItemBuilder].
     */
    fun setAttributeModifier(attribute: Attribute, modifier: AttributeModifier): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            itemMeta.addAttributeModifier(attribute, modifier)
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Removes an attribute modifier from the ItemStack object.
     *
     * @param attribute The attribute whose modifier needs to be removed.
     * @return this instance of [ItemBuilder].
     */
    fun removeAttributeModifier(attribute: Attribute): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            itemMeta.removeAttributeModifier(attribute)
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Sets an item flag for the ItemStack object.
     *
     * @param flag The flag to set.
     * @return this instance of [ItemBuilder].
     */
    fun setItemFlag(flag: ItemFlag): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            itemMeta.addItemFlags(flag)
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Sets item flags for the ItemStack object.
     *
     * @param flags The flags to set.
     * @return this instance of [ItemBuilder].
     */
    fun setItemFlag(vararg flags: ItemFlag): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            flags.toList().forEach{
                itemMeta.addItemFlags(it)
            }
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Removes an item flag from the ItemStack object.
     *
     * @param flag The flag to remove.
     * @return this instance of [ItemBuilder].
     */
    fun removeItemFlag(flag: ItemFlag): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            itemMeta.removeItemFlags(flag)
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Sets custom model data for the ItemStack object.
     *
     * @param data The custom model data to set.
     * @return this instance of [ItemBuilder].
     */
    fun setCustomModelData(data: Int): ItemBuilder
    {
        val itemMeta: ItemMeta? = itemStack.itemMeta
        if (itemMeta != null) {
            itemMeta.setCustomModelData(data)
            itemStack.itemMeta = itemMeta
        }
        return this
    }

    /**
     * Converts this [ItemBuilder] into an [ItemStack].
     *
     * @return The constructed or modified ItemStack object.
     */
    fun toItemStack(): ItemStack
    {
        return itemStack
    }
}