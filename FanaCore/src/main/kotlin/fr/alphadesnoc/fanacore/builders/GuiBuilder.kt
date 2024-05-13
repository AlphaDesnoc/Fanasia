package fr.alphadesnoc.fanacore.builders

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin

/**
 * Represents a customizable GUI with pagination for the Oasis Minecraft server plugin.
 * This GUI supports multiple pages for item display and interaction, with a dynamic number of rows per page.
 * It automatically handles the creation of "Next" and "Previous" navigation items.
 *
 * @property plugin The JavaPlugin instance this GUI is associated with, used for event registration.
 * @property name The name of the GUI, displayed as the inventory title.
 * @property rowsPerPage The number of inventory rows per page; defaults to 6.
 */
class GuiBuilder(
    plugin: JavaPlugin,
    private val name: String,
    private val rowsPerPage: Int = 6,
    private val paginationEnabled: Boolean = true
): Listener
{
    private var currentPage = 0
    private val pages: MutableList<Inventory> = mutableListOf()
    private val itemsPerPage = rowsPerPage * 9 - 9
    private val itemActions = mutableMapOf<Inventory, MutableMap<Int, ItemClickAction>>()

    init
    {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        if (paginationEnabled) {
            addPage()
        } else {
            val page = Bukkit.createInventory(null, rowsPerPage * 9, name)
            pages.add(page)
            itemActions[page] = mutableMapOf()
        }
    }

    /**
     * Adds an empty page to the GUI, with navigation items if necessary.
     * This method is automatically called when new items exceed the current page capacity.
     */
    private fun addPage()
    {
        if (!paginationEnabled) return

        val page = Bukkit.createInventory(null, rowsPerPage * 9, "$name - Page ${pages.size + 1}")

        itemActions[page] = mutableMapOf()

        if (pages.isNotEmpty()) {
            page.setItem(rowsPerPage * 9 - 9, createPreviousPageArrow())
            itemActions[page]!![rowsPerPage * 9 - 9] = object : ItemClickAction {
                override fun execute(player: Player, clickType: ClickType)
                {
                    previousPage(player)
                }
            }
        }

        pages.add(page)

        if (pages.size > 1) {
            pages[pages.size - 2].setItem(rowsPerPage * 9 - 1, createNextPageArrow())
            itemActions[pages[pages.size - 2]]!![rowsPerPage * 9 - 1] = object : ItemClickAction {
                override fun execute(player: Player, clickType: ClickType)
                {
                    nextPage(player)
                }
            }
        }
    }

    /**
     * Adds an item with a custom action to a specific page and position in the GUI.
     * If the specified page does not exist, new pages are created until the page exists.
     *
     * @param page The page number to add the item to, starting from 1.
     * @param position The position on the page to add the item to, starting from 1.
     * @param itemStack The item to add to the GUI.
     * @param action The action to associate with the item, triggered on click.
     */
    fun addItem(page: Int, position: Int, itemStack: ItemStack, action: ItemClickAction)
    {
        val finalPage = page - 1
        val finalPosition = position - 1

        while (finalPage >= pages.size) addPage()

        pages[finalPage].setItem(finalPosition, itemStack)
        itemActions[pages[finalPage]]!![finalPosition] = action
    }

    /**
     * Fills the border with a specified page in the GUI with a given item. This method is used to outline the edges
     * of a GUI page with a specific item, providing a visual border. It can optionally exclude slots that are already
     * occupied, allowing for greater flexibility in GUI design.
     *
     * @param page The 1-based page number in the GUI to fill the border with. If the specified page does not exist,
     * nothing happens.
     * @param borderItem The [ItemStack] to use as the border item. This item will be placed in the border slots
     * of the GUI page.
     * @param excludeOccupied If true, slots that are already occupied by another item will not be overwritten by
     * the border item. This is useful for preserving existing items in the GUI. If false, all border slots will
     * be filled with the border item, regardless of their current state.
     *
     * Note: This method also removes any associated actions with the slots it modifies. If a slot previously had an
     * action associated with it (e.g., a click action), and it is overwritten by this method, the associated action
     * will be removed.
     */
    fun fillBorder(page: Int, borderItem: ItemStack, excludeOccupied: Boolean = true)
    {
        val finalPage = page - 1
        if (finalPage < pages.size) {
            val inventory = pages[finalPage]
            val size = inventory.size
            val actionsMap = itemActions[inventory] ?: mutableMapOf()

            // Top row
            for (i in 0 until 9) {
                if (inventory.getItem(i) == null || !excludeOccupied) {
                    inventory.setItem(i, borderItem)
                    actionsMap.remove(i)
                }
            }
            // Bottom row
            for (i in size - 9 until size) {
                if (inventory.getItem(i) == null || !excludeOccupied) {
                    inventory.setItem(i, borderItem)
                    actionsMap.remove(i + size - 9)
                }
            }
            // Sides
            for (i in 9 until size - 9 step 9) {
                if (inventory.getItem(i) == null || !excludeOccupied) {
                    inventory.setItem(i, borderItem)
                    actionsMap.remove(i)
                }
                if (inventory.getItem(i + 8) == null || !excludeOccupied) {
                    inventory.setItem(i + 8, borderItem)
                    actionsMap.remove(i + 8)
                }
            }

            itemActions[inventory] = actionsMap
        }
    }

    /**
     * Opens the GUI for a specific player, optionally at a specified page number.
     *
     * @param player The player to open the GUI for.
     * @param page The page number to open the GUI at, defaults to the first page.
     */
    fun open(player: Player, page: Int = 0)
    {
        currentPage = page.coerceAtLeast(0).coerceAtMost(pages.size - 1)
        player.openInventory(pages[currentPage])
    }

    fun close(player: Player)
    {
        player.closeInventory()
    }

    /**
     * Handles inventory click events within the GUI.
     * This method checks if the click was in the GUI and, if so, cancels the event and triggers any associated item action.
     *
     * @param event The InventoryClickEvent to handle.
     */
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent)
    {
        val inventory = event.clickedInventory ?: return
        if (!pages.contains(inventory)) return
        event.isCancelled = true
        val rawSlot = event.rawSlot
        if (rawSlot in 0..<itemsPerPage || rawSlot == rowsPerPage * 9 - 9 || rawSlot == rowsPerPage * 9 - 1) {
            itemActions[inventory]?.get(rawSlot)?.execute(event.whoClicked as Player, event.click);
        }
    }

    private fun nextPage(player: Player)
    {
        if (!paginationEnabled || currentPage >= pages.size - 1) return

        if (currentPage < pages.size - 1) {
            open(player, currentPage + 1)
        }
    }

    private fun previousPage(player: Player)
    {
        if (!paginationEnabled) return

        if (currentPage > 0) {
            open(player, currentPage - 1)
        }
    }

    private fun createNextPageArrow(): ItemStack
    {
        val itemStack = ItemStack(Material.ARROW)
        val meta: ItemMeta? = itemStack.itemMeta
        meta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lPage Suivante"))
        itemStack.itemMeta = meta
        return itemStack
    }

    private fun createPreviousPageArrow(): ItemStack
    {
        val itemStack = ItemStack(Material.ARROW)
        val meta: ItemMeta? = itemStack.itemMeta
        meta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lPage Précédente"))
        itemStack.itemMeta = meta
        return itemStack
    }

    interface ItemClickAction
    {
        fun execute(clicker: Player, clickType: ClickType)
    }
}