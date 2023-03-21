package dev.mrshawn.deathmessages.listeners

import dev.mrshawn.deathmessages.data.PlayerData
import dev.mrshawn.deathmessages.hooks.HookManager
import dev.mrshawn.deathmessages.hooks.impl.DiscordSRVHook
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

object PlayerDeathListener: Listener {

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val playerData = PlayerData.get(event.entity)

		if (playerData.getDeathMessage() != null) {
			event.deathMessage = playerData.getDeathMessage()

			if (HookManager.isEnabled(HookManager.Hooks.DISCORDSRV)) {
				(HookManager.getHook(HookManager.Hooks.DISCORDSRV) as DiscordSRVHook).sendDiscordMessage(playerData)
			}

		}

		playerData.resetData()
	}

}