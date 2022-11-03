package dev.mrshawn.deathmessages.files

import dev.mrshawn.deathmessages.DeathMessages
import dev.mrshawn.mlib.files.KFile

object Messages: KFile(DeathMessages.instance, "messages", isResource = true)