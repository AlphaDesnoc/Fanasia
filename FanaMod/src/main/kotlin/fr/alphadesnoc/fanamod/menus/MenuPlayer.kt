package fr.alphadesnoc.fanamod.menus

import fr.alphadesnoc.fanacore.FanaCore
import fr.alphadesnoc.fanacore.builders.GuiBuilder
import fr.alphadesnoc.fanacore.builders.ItemBuilder
import fr.alphadesnoc.fanacore.builders.gradient.GradientTextBuilder
import fr.alphadesnoc.fanacore.utils.punishment.*
import fr.alphadesnoc.fanamod.FanaMod
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class MenuPlayer {

    companion object {
        fun getModerationMenu(player: Player)
        {
            val menu = GuiBuilder(FanaMod.plugin, "Moderation", 6, true)

            val slotsPerPage = 5 * 9
            var page = 0
            var slot = 0

            Bukkit.getOnlinePlayers().forEach { target: Player ->
                run {
                    if (slot >= slotsPerPage) {
                        slot = 0
                        page++
                    }

                    val item = ItemBuilder(Material.PLAYER_HEAD)
                        .setDisplayName(GradientTextBuilder().text(target.name).addColor("#22FF22").blur(0.2).build().renderText())
                        .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .setLore("Cliquez sur la tête du", "joueur afin de pouvoir", "agir dessus")
                        .toItemStack()

                    menu.addItem(page + 1, slot + 1, item, object : GuiBuilder.ItemClickAction {
                        override fun execute(clicker: Player, clickType: ClickType) {
                            getPlayerMenu(clicker, target)
                        }
                    })

                    slot++
                }
            }
            menu.open(player, 0)
        }

        fun getPlayerMenu(player: Player, target: Player)
        {
            val menuPlayer = GuiBuilder(FanaMod.plugin, "Moderation : ${target.name}", 6, false)

            val glass = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val head = ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(GradientTextBuilder().text(target.name).addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val tpItem = ItemBuilder(Material.RECOVERY_COMPASS)
                .setDisplayName(GradientTextBuilder().text("Se téléporter").addColor("#A931D3").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val actionsItem = ItemBuilder(Material.NETHERITE_SWORD)
                .setDisplayName(GradientTextBuilder().text("Actions").addColor("#E85F2F").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val moneyItem = ItemBuilder(Material.EMERALD)
                .setDisplayName(GradientTextBuilder().text("Money").addColor("#E3BB17").blur(0.2).build().renderText())
                .setLore("Money : ${FanaCore.fanaPlayerManager.getMoney(target.name)}$")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val punishmentMessage = if (FanaCore.fanaPlayerManager.hasWarn(target.name)) {
                "Nombre de sanctions: ${FanaCore.fanaPlayerManager.listPunishment(target.name).size}"
            } else {
                "Aucune sanction"
            }

            val warnItem = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Sanctions").addColor("#1789E3").blur(0.2).build().renderText())
                .setLore(punishmentMessage)
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val closeItem = ItemBuilder(Material.BARRIER)
                .setDisplayName(GradientTextBuilder().text("Fermer la page").addColor("#A92525").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            menuPlayer.fillBorder(1, glass)
            menuPlayer.addItem(1, 14, head, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {}
            })

            menuPlayer.addItem(1, 30, tpItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    clicker.teleport(target.location)
                    clicker.sendMessage("Vous avez été téléporté à ${target.name}")
                }
            })

            menuPlayer.addItem(1, 31, actionsItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    getAddPunishmentMenu(clicker, target)
                }
            })

            menuPlayer.addItem(1, 33, moneyItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                }
            })

            menuPlayer.addItem(1, 34, warnItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    if (FanaCore.fanaPlayerManager.hasWarn(target.name)) {
                        getPunishmentMenu(clicker, target)
                    }
                }
            })

            menuPlayer.addItem(1, 41, closeItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    menuPlayer.close(clicker)
                }
            })

            menuPlayer.open(player, 0)
        }

        fun getPunishmentMenu(player: Player, target: Player)
        {
            val punishmentMenu = GuiBuilder(FanaMod.plugin, "Sanctions de ${target.name}", 6, false)

            val slotsPerPage = 5 * 9
            var page = 0
            var slot = 0

            FanaCore.fanaPlayerManager.listPunishment(target.name).forEach { punishement: Punishment ->
                run {
                    if (slot >= slotsPerPage) {
                        slot = 0
                        page++
                    }

                    val mat = when (punishement.type) {
                        PunishmentType.WARN -> Material.BOOK
                        PunishmentType.BAN -> Material.NETHERITE_SWORD
                        PunishmentType.TEMP_BAN -> Material.IRON_SWORD
                        PunishmentType.MUTE -> Material.MUSIC_DISC_5
                        PunishmentType.KICK -> Material.BLAZE_ROD
                    }

                    val punishmentItem = ItemBuilder(mat)
                        .setDisplayName(GradientTextBuilder().text("Type : ${punishement.type.desc}").addColor("#22FF22").blur(0.2).build().renderText())
                        .setLore("Raison : ${getPunishmentDescription(punishement.reason)}\n", "Date : ${punishement.date}")
                        .setItemFlag(
                            ItemFlag.HIDE_ATTRIBUTES,
                            ItemFlag.HIDE_POTION_EFFECTS
                        )
                        .toItemStack()

                    punishmentMenu.addItem(page + 1, slot + 1, punishmentItem, object : GuiBuilder.ItemClickAction {
                        override fun execute(clicker: Player, clickType: ClickType) {
                            getRemovePunishmentMenu(clicker, target, punishement)
                        }
                    })

                    slot++
                }
            }
            punishmentMenu.open(player, 0)
        }

        fun getRemovePunishmentMenu(player: Player, target: Player, punishment: Punishment) {
            val removePunishmentMenu = GuiBuilder(FanaMod.plugin, "Sanctions de ${target.name}", 5, false)

            val glass = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val mat = when (punishment.type) {
                PunishmentType.WARN -> Material.BOOK
                PunishmentType.BAN -> Material.NETHERITE_SWORD
                PunishmentType.TEMP_BAN -> Material.IRON_SWORD
                PunishmentType.MUTE -> Material.MUSIC_DISC_5
                PunishmentType.KICK -> Material.BLAZE_ROD
            }

            val punishmentItem = ItemBuilder(mat)
                .setDisplayName(GradientTextBuilder().text("Type : ${punishment.type.desc}").addColor("#22FF22").blur(0.2).build().renderText())
                .setLore("Raison : ${getPunishmentDescription(punishment.reason)}\n", "Date : ${punishment.date}\n", "Durée : ${getPunishmentDuration(punishment.duration)}")
                .setItemFlag(
                    ItemFlag.HIDE_ATTRIBUTES,
                    ItemFlag.HIDE_POTION_EFFECTS
                )
                .toItemStack()

            val remove = ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .setDisplayName(GradientTextBuilder().text("Supprimer la sanction").addColor("#467E0E").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val close = ItemBuilder(Material.BARRIER)
                .setDisplayName(GradientTextBuilder().text("Fermer la page").addColor("#A92525").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            removePunishmentMenu.fillBorder(1, glass)
            removePunishmentMenu.addItem(1, 14, punishmentItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {}
            })

            removePunishmentMenu.addItem(1, 23, remove, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    FanaCore.fanaPlayerManager.removePunishment(target.name, punishment)
                    getPunishmentMenu(clicker, target)
                }
            })

            removePunishmentMenu.addItem(1, 32, close, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    removePunishmentMenu.close(clicker)
                }
            })

            removePunishmentMenu.open(player, 0)

        }

        fun getAddPunishmentMenu(player: Player, target: Player) {
            val addPunishmentMenu = GuiBuilder(FanaMod.plugin, "Sanction pour ${target.name}", 6, false)

            val glass = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val head = ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(GradientTextBuilder().text(target.name).addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val warn = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Avertissement").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val ban = ItemBuilder(Material.NETHERITE_SWORD)
                .setDisplayName(GradientTextBuilder().text("Bannissement Définitif").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val temp_ban = ItemBuilder(Material.IRON_SWORD)
                .setDisplayName(GradientTextBuilder().text("Bannissement Temporaire").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val mute = ItemBuilder(Material.MUSIC_DISC_5)
                .setDisplayName(GradientTextBuilder().text("Rendre Muet").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                .toItemStack()

            val kick = ItemBuilder(Material.BLAZE_ROD)
                .setDisplayName(GradientTextBuilder().text("Expulser").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val closeItem = ItemBuilder(Material.BARRIER)
                .setDisplayName(GradientTextBuilder().text("Fermer la page").addColor("#A92525").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()


            addPunishmentMenu.fillBorder(1, glass)
            addPunishmentMenu.addItem(1, 14, head, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {}
            })

            addPunishmentMenu.addItem(1, 30, warn, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    FanaCore.fanaPlayerManager.addPunishment(target.name, PunishmentType.WARN, PunishmentReason.Warn(WarnReason.GLOBAL.desc), PunishmentDuration.NULL)
                    target.sendMessage("Vous avez reçu un avertissement de ${clicker.name}")
                }
            })

            addPunishmentMenu.addItem(1, 31, ban, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    getAddBanMenu(clicker, target)
                }
            })

            addPunishmentMenu.addItem(1, 32, temp_ban, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    getAddTempBanMenu(clicker, target)
                }
            })

            addPunishmentMenu.addItem(1, 33, kick, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    target.kickPlayer("Vous avez été expulsé par ${clicker.name}")
                    FanaCore.fanaPlayerManager.addPunishment(target.name, PunishmentType.KICK, PunishmentReason.Kick(KickReason.GLOBAL.desc), PunishmentDuration.NULL)
                }
            })

            addPunishmentMenu.addItem(1, 34, mute, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    getAddMuteMenu(clicker, target)
                }
            })

            addPunishmentMenu.addItem(1, 41, closeItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    addPunishmentMenu.close(clicker)
                }
            })

            addPunishmentMenu.open(player, 0)
        }

        fun getAddMuteMenu(player: Player, target: Player) {
            val addMuteMenu = GuiBuilder(FanaMod.plugin, "Sanction pour ${target.name}", 6, false)

            val glass = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val head = ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(GradientTextBuilder().text(target.name).addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val dpMaj = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Double Post / MAJ").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val spam = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Spam / Flood").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val provoc = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Provoc / Langage").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val insulLeg = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Insulte légère").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val insulLourd = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Insulte lourde").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val pubYT = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Pub (youtube, discord)").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val pubServ = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Pub serveur").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val closeItem = ItemBuilder(Material.BARRIER)
                .setDisplayName(GradientTextBuilder().text("Fermer la page").addColor("#A92525").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            addMuteMenu.fillBorder(1, glass)
            addMuteMenu.addItem(1, 14, head, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {}
            })

            addMuteMenu.addItem(1, 22, dpMaj, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    mutePlayer(clicker, target, PunishmentReason.Mute(MuteReason.DP_MAJ.desc), PunishmentDuration.MIN30)
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.addItem(1, 23, spam, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    mutePlayer(clicker, target, PunishmentReason.Mute(MuteReason.SPAM_FLOOD.desc), PunishmentDuration.MIN30)
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.addItem(1, 24, provoc, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    mutePlayer(clicker, target, PunishmentReason.Mute(MuteReason.PROVOC_LANGUAGE.desc), PunishmentDuration.HOUR1)
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.addItem(1, 30, insulLeg, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    mutePlayer(clicker, target, PunishmentReason.Mute(MuteReason.INSULTE_LEGERE.desc), PunishmentDuration.HOUR2)
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.addItem(1, 31, insulLourd, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    mutePlayer(clicker, target, PunishmentReason.Mute(MuteReason.INSULTE_LOURDE.desc), PunishmentDuration.HOUR3)
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.addItem(1, 33, pubYT, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    mutePlayer(clicker, target, PunishmentReason.Mute(MuteReason.PUB_YT_DISCORD.desc), PunishmentDuration.DAY10)
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.addItem(1, 34, pubServ, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    mutePlayer(clicker, target, PunishmentReason.Mute(MuteReason.PUB_SERV.desc), PunishmentDuration.DAY20)
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.addItem(1, 41, closeItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    addMuteMenu.close(clicker)
                }
            })

            addMuteMenu.open(player, 0)
        }

        fun getAddBanMenu(player: Player, target: Player) {
            val addBanMenu = GuiBuilder(FanaMod.plugin, "Sanction pour ${target.name}", 6, false)

            val glass = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val head = ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(GradientTextBuilder().text(target.name).addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val usurp = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Usurpation D'identité").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val glitch = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Glitch").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val dupli = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Duplication").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val faille = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Faille").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val hack = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Hack de compte").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val closeItem = ItemBuilder(Material.BARRIER)
                .setDisplayName(GradientTextBuilder().text("Fermer la page").addColor("#A92525").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            addBanMenu.fillBorder(1, glass)
            addBanMenu.addItem(1, 14, head, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {}
            })

            addBanMenu.addItem(1, 30, usurp, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    banPlayer(target, PunishmentReason.Ban(BanReason.USURP.desc), PunishmentDuration.INFINITE)
                    addBanMenu.close(clicker)
                }
            })

            addBanMenu.addItem(1, 31, glitch, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    banPlayer(target, PunishmentReason.Ban(BanReason.GLITCH.desc), PunishmentDuration.INFINITE)
                    addBanMenu.close(clicker)
                }
            })

            addBanMenu.addItem(1, 32, dupli, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    banPlayer(target, PunishmentReason.Ban(BanReason.DUPLI.desc), PunishmentDuration.INFINITE)
                    addBanMenu.close(clicker)
                }
            })

            addBanMenu.addItem(1, 33, faille, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    banPlayer(target, PunishmentReason.Ban(BanReason.FAILLE.desc), PunishmentDuration.INFINITE)
                    addBanMenu.close(clicker)
                }
            })

            addBanMenu.addItem(1, 34, hack, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    banPlayer(target, PunishmentReason.Ban(BanReason.HACK.desc), PunishmentDuration.INFINITE)
                    addBanMenu.close(clicker)
                }
            })

            addBanMenu.addItem(1, 41, closeItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    addBanMenu.close(clicker)
                }
            })

            addBanMenu.open(player, 0)
        }

        fun getAddTempBanMenu(player: Player, target: Player) {
            val addTempBanMenu = GuiBuilder(FanaMod.plugin, "Sanction pour ${target.name}", 6, false)

            val glass = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val head = ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(GradientTextBuilder().text(target.name).addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val afk1 = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("AFK Farm/Mine").addColor("#22FF22").blur(0.2).build().renderText())
                .setLore("1ere fois")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val afk2 = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("AFK Farm/Mine").addColor("#22FF22").blur(0.2).build().renderText())
                .setLore("2eme fois")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val afk3 = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("AFK Farm/Mine").addColor("#22FF22").blur(0.2).build().renderText())
                .setLore("3eme fois")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val afk4 = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("AFK Farm/Mine").addColor("#22FF22").blur(0.2).build().renderText())
                .setLore("4eme fois")
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val cheat = ItemBuilder(Material.BOOK)
                .setDisplayName(GradientTextBuilder().text("Cheat + logiciel externe").addColor("#22FF22").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            val closeItem = ItemBuilder(Material.BARRIER)
                .setDisplayName(GradientTextBuilder().text("Fermer la page").addColor("#A92525").blur(0.2).build().renderText())
                .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack()

            addTempBanMenu.fillBorder(1, glass)
            addTempBanMenu.addItem(1, 14, head, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {}
            })

            addTempBanMenu.addItem(1, 30, afk1, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    handleKickOrBan(player, afk1)
                    addTempBanMenu.close(clicker)
                }
            })

            addTempBanMenu.addItem(1, 31, afk2, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    handleKickOrBan(player, afk2)
                    addTempBanMenu.close(clicker)
                }
            })

            addTempBanMenu.addItem(1, 32, afk3, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    handleKickOrBan(player, afk3)
                    addTempBanMenu.close(clicker)
                }
            })

            addTempBanMenu.addItem(1, 33, afk4, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    handleKickOrBan(player, afk4)
                    addTempBanMenu.close(clicker)
                }
            })

            addTempBanMenu.addItem(1, 34, cheat, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    tempBanPlayer(target, PunishmentReason.TempBan(TempBanReason.CHEAT.desc), PunishmentDuration.DAY30)
                    addTempBanMenu.close(clicker)
                }
            })

            addTempBanMenu.addItem(1, 41, closeItem, object : GuiBuilder.ItemClickAction {
                override fun execute(clicker: Player, clickType: ClickType) {
                    addTempBanMenu.close(clicker)
                }
            })

            addTempBanMenu.open(player, 0)
        }

        private fun getPunishmentDescription(reason: PunishmentReason): String? {
            return when (reason) {
                is PunishmentReason.Warn -> reason.desc
                is PunishmentReason.Mute -> reason.desc
                is PunishmentReason.Ban -> reason.desc
                is PunishmentReason.TempBan -> reason.desc
                is PunishmentReason.Kick -> reason.desc
                else -> null
            }
        }

        private fun mutePlayer(modo: Player, player: Player, reason: PunishmentReason, duration: PunishmentDuration) {
            val targetName = player.name
            FanaCore.fanaPlayerManager.addPunishment(targetName, PunishmentType.MUTE, PunishmentReason.Mute(MuteReason.DP_MAJ.desc), duration)
            FanaCore.mutedPlayer[targetName] = getLongDuration(duration)
            player.sendMessage("Vous avez été mute par ${modo.name} pendant ${duration.format}")
        }

        private fun getPunishmentDuration(duration: PunishmentDuration): String {
            return duration.format
        }

        private fun getLongDuration(duration: PunishmentDuration): Long {
            return duration.time + System.currentTimeMillis()
        }

        private fun getDateByDuration(duration: PunishmentDuration): String
        {
            return SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date(getLongDuration(duration)))
        }

        private fun banPlayer(player: Player, reason: PunishmentReason, duration: PunishmentDuration) {
            FanaCore.fanaPlayerManager.addPunishment(player.name, PunishmentType.BAN, reason, duration)
            FanaCore.fanaPlayerManager.setBanned(player.name)
            val builder = StringBuilder()
            builder.append("Vous avez été banni\n")
            builder.append("Pour : ${getPunishmentDescription(reason)}\n")
            builder.append(getPunishmentDuration(duration))
            player.kickPlayer(builder.toString())
        }

        private fun tempBanPlayer(player: Player, reason: PunishmentReason, duration: PunishmentDuration) {
            FanaCore.fanaPlayerManager.addPunishment(player.name, PunishmentType.TEMP_BAN, reason, duration)
            FanaCore.fanaPlayerManager.setTempBanned(player.name, getDateByDuration(duration))
            val builder = StringBuilder()
            builder.append("Vous avez été banni\n")
            builder.append("Pour : ${getPunishmentDescription(reason)}\n")
            builder.append("Jusqu'au : ${getDateByDuration(duration)}")
            player.kickPlayer(builder.toString())
        }

        private fun kickPlayer(player: Player, reason: PunishmentReason) {
            FanaCore.fanaPlayerManager.addPunishment(player.name, PunishmentType.KICK, reason, PunishmentDuration.NULL)
            val builder = StringBuilder()
            builder.append("Vous avez été kick\n")
            builder.append("Pour : ${getPunishmentDescription(reason)}")
            player.kickPlayer(builder.toString())
        }

        private fun handleKickOrBan(player: Player, item: ItemStack)
        {
            if(item.itemMeta!!.lore!!.contains("1ere fois")) {
                kickPlayer(player, PunishmentReason.Kick(KickReason.FARMAFK.desc))
            }
            else if (item.itemMeta!!.lore!!.contains("2eme fois")) {
                val punishmentsCopy = ArrayList(FanaCore.fanaPlayerManager.listPunishment(player.name))
                punishmentsCopy.forEach { punishment ->
                    if (punishment.type == PunishmentType.KICK && punishment.reason == PunishmentReason.Kick(KickReason.FARMAFK.desc)) {
                        tempBanPlayer(
                            player,
                            PunishmentReason.TempBan(TempBanReason.FARMAFK.desc),
                            PunishmentDuration.DAY1
                        )
                    }
                    else {
                        return
                    }
                }
            }
            else if (item.itemMeta!!.lore!!.contains("3eme fois")) {
                val punishmentsCopy = ArrayList(FanaCore.fanaPlayerManager.listPunishment(player.name))
                punishmentsCopy.forEach { punishment ->
                    if (punishment.type == PunishmentType.KICK && punishment.reason == PunishmentReason.Kick(KickReason.FARMAFK.desc) && punishment.duration == PunishmentDuration.DAY1) {
                        tempBanPlayer(
                            player,
                            PunishmentReason.TempBan(TempBanReason.FARMAFK.desc),
                            PunishmentDuration.DAY3
                        )
                    }
                    else {
                        return
                    }
                }
            }
            else if (item.itemMeta!!.lore!!.contains("4eme fois")) {
                val punishmentsCopy = ArrayList(FanaCore.fanaPlayerManager.listPunishment(player.name))
                punishmentsCopy.forEach { punishment ->
                    if (punishment.type == PunishmentType.KICK && punishment.reason == PunishmentReason.Kick(KickReason.FARMAFK.desc) && punishment.duration == PunishmentDuration.DAY3) {
                        tempBanPlayer(
                            player,
                            PunishmentReason.TempBan(TempBanReason.FARMAFK.desc),
                            PunishmentDuration.DAY7
                        )
                    }
                    else {
                        return
                    }
                }
            }
        }

    }

}