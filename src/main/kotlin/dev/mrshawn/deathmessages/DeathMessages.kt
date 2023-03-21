package dev.mrshawn.deathmessages

import co.aikar.commands.PaperCommandManager
import dev.mrshawn.deathmessages.commands.DeathMessagesCMD
import dev.mrshawn.deathmessages.data.PlayerData
import dev.mrshawn.deathmessages.hooks.HookManager
import dev.mrshawn.deathmessages.listeners.EntityDamageListener
import dev.mrshawn.deathmessages.listeners.PlayerDeathListener
import dev.mrshawn.mlib.selections.Selection
import dev.mrshawn.mlib.utilities.events.EventUtils
import org.bukkit.plugin.java.JavaPlugin

class DeathMessages: JavaPlugin() {

	companion object {
		lateinit var instance: DeathMessages
	}

	override fun onEnable() {
		instance = this

		initObjects()
		registerListeners()
		registerCommands()

		HookManager.loadHooks()
	}

	override fun onDisable() {

	}

	private fun registerCommands() {
		val pcm = PaperCommandManager(this)

		pcm.registerCommand(DeathMessagesCMD())
	}

	private fun registerListeners() {
		Selection.register(this)
		EventUtils.registerEvents(
			this,
			PlayerDeathListener,
			EntityDamageListener,
			PlayerData.Companion
		)
	}

	/**
	 * Access kotlin objects that we need the init to run on plugin load
	 */
	private fun initObjects() { }

}