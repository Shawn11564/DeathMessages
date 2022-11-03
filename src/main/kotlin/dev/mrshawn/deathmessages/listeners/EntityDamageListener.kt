package dev.mrshawn.deathmessages.listeners

import dev.mrshawn.deathmessages.data.PlayerData
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityDamageListener: Listener {

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entityType != EntityType.PLAYER) return

		val player = event.entity as Player
		val playerData = PlayerData.get(player)

		if (event.damager.type == (playerData.lastDamager?.type ?: false)) {
			// Player damaged by same entity type
			if (!playerData.lastDamagerList.contains(event.damager.uniqueId)) {
				// Player damaged by same entity type, but not the same entity
				playerData.lastDamagerList.add(event.damager.uniqueId)
			}
		} else {
			// Player damaged by different entity type
			playerData.lastDamagerList.clear()
			playerData.lastDamagerList.add(event.damager.uniqueId)
		}

		playerData.lastDamager = event.damager

		if (player.health - event.finalDamage <= 0) {
			/**
			 * Player has died
			 */
		}
	}

}