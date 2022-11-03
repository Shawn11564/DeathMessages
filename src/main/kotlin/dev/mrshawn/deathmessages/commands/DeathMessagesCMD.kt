package dev.mrshawn.deathmessages.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Subcommand
import dev.mrshawn.deathmessages.files.Messages
import dev.mrshawn.mlib.chat.Chat
import org.bukkit.command.CommandSender

@CommandAlias("deathmessages|dm")
class DeathMessagesCMD: BaseCommand() {

	@Subcommand("reload")
	fun onReload(sender: CommandSender) {
		Messages.reload()
		Chat.tell(sender, "&aReloaded DeathMessages!")
	}

}