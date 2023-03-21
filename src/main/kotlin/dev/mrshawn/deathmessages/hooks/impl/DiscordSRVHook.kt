package dev.mrshawn.deathmessages.hooks.impl

import dev.mrshawn.deathmessages.data.PlayerData
import dev.mrshawn.deathmessages.files.Config
import dev.mrshawn.deathmessages.files.Messages
import dev.mrshawn.deathmessages.hooks.Hook
import dev.mrshawn.mlib.chat.Chat
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed
import github.scarsz.discordsrv.util.DiscordUtil
import java.time.Instant

class DiscordSRVHook: Hook {

	override fun onEnable() {
		Chat.log("&aDiscordSRV found! Hooking...")
	}

	fun sendDiscordMessage(playerData: PlayerData) {
		if (Config.getBoolean(Config.CValues.DISCORD_ENABLED) == false) return
		if (Config.getBoolean("discord.messages.${playerData.getMessageType().name.lowercase()}.enabled") == false) return
		Config.getStringList("discord.messages.${playerData.getMessageType().name.lowercase()}.channels").forEach { channel ->
			val split = channel.split(":")
			val guildID = split[0]
			val channelID = split[1]

			val guild = DiscordUtil.getJda().getGuildById(guildID)
			if (guild == null) {
				Chat.error("Guild with ID $guildID not found!")
				return
			}

			val textChannel = guild.getTextChannelById(channelID)
			if (textChannel == null) {
				Chat.error("Text channel with ID $channelID in guild $guildID not found!")
				return
			}
			if (!textChannel.canTalk()) {
				Chat.error("Cannot talk in channel $channelID in guild $guildID!")
				return
			}

			val embed = buildDeathMessageEmbed(playerData)

			textChannel.sendMessageEmbeds(embed).queue()
		}
	}

	fun buildDeathMessageEmbed(playerData: PlayerData): MessageEmbed {
		return EmbedBuilder()
			.setAuthor(
				Messages.getString("discord.author.name")?.replace("%message%", playerData.getDeathMessage() ?: "unknown"),
				Messages.getString("discord.author.url")?.replace("%uuid%", playerData.player.uniqueId.toString()),
				Messages.getString("discord.author.icon-url")?.replace("%uuid%", playerData.player.uniqueId.toString())
			)
			.setTitle(Messages.getString("discord.title"))
			.setDescription(Messages.getString("discord.description")?.replace("%message%", playerData.getDeathMessage() ?: "unknown"))
			.setImage(Messages.getString("discord.image")?.replace("%uuid%", playerData.player.uniqueId.toString()))
			.setTimestamp(Instant.now())
			.build()
	}

}
