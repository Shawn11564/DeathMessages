package dev.mrshawn.deathmessages.files

import dev.mrshawn.deathmessages.DeathMessages
import dev.mrshawn.mlib.files.KFile

object Config: KFile(DeathMessages.instance, "config", isResource = true) {

	enum class CValues(private val path: String, private val default: Any?): IConfigList {
		GANGS_SIZE("gangs.size", 2);

		override fun getPath(): String {
			return path
		}

		override fun getDefault(): Any? {
			return default
		}
	}

}