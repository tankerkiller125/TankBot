package dev.kilgore.bots.discord.tankbot

import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.util.DiscordException

object Bot {
    var clientBuilder: ClientBuilder? = null

    fun createClient(token: String, login: Boolean): IDiscordClient? {
        clientBuilder = ClientBuilder().withRecommendedShardCount().withToken(token)
        return try {
            if (login) {
                clientBuilder!!.login()
            } else {
                clientBuilder!!.build()
            }
        } catch (e: DiscordException) {
            e.printStackTrace()
            null
        }
    }
}