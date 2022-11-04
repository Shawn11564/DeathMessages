package dev.mrshawn.deathmessages.data

import dev.mrshawn.deathmessages.files.Aliases
import dev.mrshawn.deathmessages.files.Config
import dev.mrshawn.deathmessages.files.Messages
import dev.mrshawn.mlib.chat.Chat
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class PlayerData(
	private val player: Player
) {

	companion object: Listener {
		private val playerDataMap = mutableMapOf<Player, PlayerData>()

		fun get(player: Player): PlayerData {
			return playerDataMap.getOrPut(player) { PlayerData(player) }
		}

		@EventHandler
		fun onPlayerQuit(event: PlayerQuitEvent) {
			playerDataMap.remove(event.player)
		}
	}

	var lastDamager: Entity? = null
	val lastDamagerList: MutableList<UUID> = mutableListOf()

	fun resetData() {
		lastDamager = null
		lastDamagerList.clear()
	}

	fun getDeathMessage(): String? {
		val lastDamageCause = player.lastDamageCause?.cause
		var message: String? = null

		/**
		 * Get the proper death message
		 */
		if (lastDamageCause != null) {
			message = if (lastDamageCause == DamageCause.ENTITY_ATTACK && lastDamager?.type != EntityType.PLAYER) {
				Messages.getString("mobs." +
						"${lastDamager!!.type.name.lowercase()}." +
						if (lastDamagerList.size >= Config.getInt(Config.CValues.GANGS_SIZE)!!) "gang" else "solo"
				)
			} else {
				Messages.getString("natural.${lastDamageCause.name.lowercase()}")
			}
		}

		/**
		 * Do message replacements
		 */
		if (message != null) {
			message = message.replace("%player%", player.name)

			message = doReplacements(message)

			message = Chat.colorize(message)
		}

		if (message == null && Config.getBoolean(Config.CValues.USE_DEFAULT)!!) {
			message = Chat.colorize(Messages.getString("default")
							?.replace("%player%", player.name))
		}

		return message
	}

	private fun doReplacements(message: String?): String? {
		if (message == null) return null
		var processedMessage = message
		val lastDamageCause = player.lastDamageCause?.cause

		lastDamageCause?.let {
			when (lastDamageCause) {
				DamageCause.ENTITY_ATTACK -> {
					lastDamager?.let {
						processedMessage = message.replace(
							"%killer%",
							if (it.type == EntityType.PLAYER) {
								(it as Player).name
							} else {
								Aliases.getString(it.type) ?: it.type.name
							}
						)
					}
				}
				DamageCause.CONTACT -> {
					processedMessage = message.replace(
						"%block%",
						(player.lastDamageCause!! as EntityDamageByBlockEvent)
							.damager?.type?.name?.lowercase()?.replace("_", " ") ?: "unknown"
					)
				}
				DamageCause.PROJECTILE -> {
					processedMessage = message.replace(
						"%projectile%",
						Aliases.getString(
							(player.lastDamageCause!! as EntityDamageByEntityEvent).damager.type
						) ?: "unknown"
					)
				}
				DamageCause.BLOCK_EXPLOSION -> {
					processedMessage = message.replace(
						"%block%",
						(player.lastDamageCause!! as EntityDamageByBlockEvent)
							.damager?.type?.name?.lowercase() ?: "unknown"
					)
				}
				DamageCause.ENTITY_EXPLOSION -> {
					processedMessage = message.replace(
						"%killer%",
						Aliases.getString(
							(player.lastDamageCause!! as EntityDamageByEntityEvent).damager.type
						) ?: "unknown"
					)
				}
				DamageCause.FALLING_BLOCK -> {
					processedMessage = message.replace(
						"%block%",
						((player.lastDamageCause!! as EntityDamageByEntityEvent)
							.damager as FallingBlock).blockData.material.name.lowercase()
					)
				}
				else -> {}
			}
		}

		return processedMessage
	}

}