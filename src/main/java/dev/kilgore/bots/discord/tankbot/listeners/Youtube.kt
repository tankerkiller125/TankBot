package dev.kilgore.bots.discord.tankbot.listeners

import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTube.Builder
import com.google.api.services.youtube.YouTube.Videos.List
import com.google.api.services.youtube.model.Video
import com.google.common.base.Splitter
import dev.kilgore.bots.discord.tankbot.Main.client
import dev.kilgore.bots.discord.tankbot.Main.config
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.EmbedBuilder
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern

class Youtube : IListener<MessageReceivedEvent> {
    override fun handle(event: MessageReceivedEvent) {
        val command = event.message.content.startsWith("-youtube")
        val channel: Long = event.message.channel.longID
        val regex: Pattern = Pattern.compile("https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*", Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = regex.matcher(event.message.content)
        if (command) {
            try {
                client!!.getChannelByID(channel).sendMessage("Youtube command not yet implemented")
            } catch (e: Exception) {
                println(e.message)
            }
        } else {
            if (matcher.find()) {
                try {
                    event.message.delete();
                    val v = getStats(event.message.content.substring(matcher.start(), matcher.end()))
                    val builder = EmbedBuilder()
                    builder.withTitle(v.snippet.title)
                            .withColor(255,0,0)
                            .withUrl(event.message.content.substring(matcher.start(), matcher.end()))
                            .withAuthorName(event.message.author.name)
                            .withAuthorIcon(event.message.author.avatarURL)
                            .appendField("Views", v.statistics.viewCount.toString(), true)
                            .appendField("Likes", v.statistics.likeCount.toString(), true)
                            .appendField("Dislikes", v.statistics.dislikeCount.toString(), true)
                            .appendField("Comments", v.statistics.commentCount.toString(), true)
                    client!!.getChannelByID(channel).sendMessage(builder.build())
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    @Throws(MalformedURLException::class)
    private fun getVideoID(videoUrl: String): String {
        val url = URL(videoUrl)
        val query: String? = url.query
        val map: Map<String?, String?> = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query)
        return map["v"] ?: error("Could not get Video ID")
    }

    @Throws(IOException::class)
    private fun getStats(videoURL: String): Video {
        youtube = Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpRequestInitializer { }).setApplicationName("TankBot").build()
        val list: List = youtube!!.videos().list("statistics,snippet").setId(getVideoID(videoURL)).setKey(config!!.getString("apiKeys.youtube"))
        return list.execute().items[0]
    }

    companion object {
        private val HTTP_TRANSPORT: HttpTransport = NetHttpTransport()
        private val JSON_FACTORY: JsonFactory = JacksonFactory()
        private var youtube: YouTube? = null
    }
}