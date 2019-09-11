package dev.kilgore.bots.discord.tankbot.listeners

import dev.kilgore.bots.discord.tankbot.Main.client
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.EmbedBuilder
import java.lang.Exception
import java.util.regex.Pattern

class XKCDLinker : IListener<MessageReceivedEvent> {
    override fun handle(event: MessageReceivedEvent?) {
        val channel = event!!.message.channel.longID

        val patternString = "(?:(?:http|https)(?::\\\\/{2}[\\\\w]+)(?:[\\\\/|\\\\.]?)(?:[^\\\\s\\\"]*))(?:xkcd\\\\.com).*?(\\\\d+)"

        val pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE or Pattern.DOTALL or Pattern.MULTILINE)
        val matcher = pattern.matcher(event.message.content)

        val builder = EmbedBuilder()

        if (matcher.find()) {
            matcher.reset()
            while (matcher.find()) {
                val comicID = matcher.group(1).toInt()
                try {
                    client!!.getChannelByID(channel).sendMessage(comicID.toString())
                } catch (exception: Exception) {

                }
            }
        }
    }

}