package dev.kilgore.bots.discord.tankbot

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import dev.kilgore.bots.discord.tankbot.commands.*
import dev.kilgore.bots.discord.tankbot.commands.admin.*
import dev.kilgore.bots.discord.tankbot.commands.admin.settings.*
import dev.kilgore.bots.discord.tankbot.listeners.*
import dev.kilgore.bots.discord.tankbot.util.NewHandler
import sx.blah.discord.api.IDiscordClient
import kotlin.system.exitProcess

object Main {
    var client: IDiscordClient? = null
    var handler: NewHandler? = null
    var config: Config? = null
    private var apiKey: String? = null

    /**
     * Main function that starts the bot
     *
     * @param args Array<String>
     * @return void
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val environment = System.getenv("ENV")
        config = if (environment.isEmpty()) {
            println("Environment: Default")
            ConfigFactory.load()
        } else {
            println("Environment: $environment")
            ConfigFactory.load(environment)
        }
        apiKey = when {
            config!!.getString("discord.token").isNotEmpty() -> config!!.getString("discord.token")
            args.isNotEmpty() -> args[0]
            else -> {
                println("You need to enter a key")
                exitProcess(0)
            }
        }

        client = Bot.createClient(apiKey!!, true)
        handler = NewHandler(client)
        handler!!.defaultPrefix = "!"

        handler!!.loadPermissions()

        // Register Commands
        handler!!.registerCommand(Exit())
        handler!!.registerCommand(AddPermission(handler!!))
        handler!!.registerCommand(RemovePermission(handler!!))
        handler!!.registerCommand(ChangeGame())
        handler!!.registerCommand(ChangeAvatar())

        // Help Command
        handler!!.registerCommand(Help(handler!!))

        // Listeners
        client!!.dispatcher.registerListener(Youtube())

        Runtime.getRuntime().addShutdownHook(Thread {
            fun run() {
                handler!!.savePermissions()
                print("Permissions saved!")
            }
        })
    }
}