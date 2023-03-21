package dev.mrshawn.deathmessages.hooks

import dev.mrshawn.deathmessages.hooks.impl.DiscordSRVHook
import org.bukkit.Bukkit

object HookManager {

	private val hooks = HashMap<String, Hook>()

	fun loadHooks() {
		Hooks.values().forEach { hook ->
			if (Bukkit.getPluginManager().getPlugin(hook.pluginName) != null) {
				val instance = hook.clazz.newInstance()

				hooks[hook.pluginName] = instance
				instance.onEnable()
			}
		}
	}

	fun isEnabled(hook: Hooks): Boolean {
		return hooks.containsKey(hook.pluginName)
	}

	fun getHook(hook: Hooks): Hook? {
		return hooks[hook.pluginName]
	}

	enum class Hooks(val pluginName: String, val clazz: Class<out Hook>) {
		DISCORDSRV("DiscordSRV", DiscordSRVHook::class.java)
	}

}