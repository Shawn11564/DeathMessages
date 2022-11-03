package dev.mrshawn.deathmessages.files

import dev.mrshawn.deathmessages.DeathMessages
import dev.mrshawn.mlib.files.KFile
import org.bukkit.entity.EntityType

object Aliases: KFile(DeathMessages.instance, "aliases", isResource = true) {

	fun getString(entityType: EntityType): String? {
		return getString(entityType.name)
	}

}