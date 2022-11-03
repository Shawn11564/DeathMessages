package dev.mrshawn.deathmessages.listeners

import dev.mrshawn.deathmessages.data.PlayerData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

object PlayerDeathListener: Listener {

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val playerData = PlayerData.get(event.entity)

		if (playerData.getDeathMessage() != null) {
			event.deathMessage = playerData.getDeathMessage()
		}

		playerData.resetData()
	}

}