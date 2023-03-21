package dev.mrshawn.deathmessages.files

import dev.mrshawn.deathmessages.DeathMessages
import dev.mrshawn.mlib.files.KFile

object Config: KFile(DeathMessages.instance, "config", isResource = true) {

	enum class CValues(private val path: String, private val default: Any?): IConfigList {
		GANGS_SIZE("gangs.size", 2),
		USE_DEFAULT("messages.use-default", false),
		DISCORD_ENABLED("discord.enabled", true),
		DISCORD_PLAYER_ENABLED("discord.messages.player", true),
		DISCORD_MOB_ENABLED("discord.messages.mob", true),
		DISCORD_ENTITY_ENABLED("discord.messages.entity", true),
		DISCORD_NATURAL_ENABLED("discord.messages.natural", true);

		override fun getPath(): String {
			return path
		}

		override fun getDefault(): Any? {
			return default
		}
	}

}