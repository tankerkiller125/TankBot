package dev.kilgore.bots.discord.tankbot

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import dev.kilgore.bots.discord.tankbot.commands.Help
import dev.kilgore.bots.discord.tankbot.commands.admin.AddPermission
import dev.kilgore.bots.discord.tankbot.commands.admin.Exit
import dev.kilgore.bots.discord.tankbot.commands.admin.RemovePermission
import dev.kilgore.bots.discord.tankbot.commands.admin.settings.ChangeAvatar
import dev.kilgore.bots.discord.tankbot.commands.admin.settings.ChangeGame
import dev.kilgore.bots.discord.tankbot.listeners.XKCDLinker
import dev.kilgore.bots.discord.tankbot.listeners.Youtube
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
    @JvmStatic fun main(args: Array<String>) {
        val environment = System.getenv("ENV")
        if (environment.isEmpty()) {
            System.out.println("Environment: Default")
            config = ConfigFactory.load()
        } else {
            System.out.println("Environment: $environment")
            config = ConfigFactory.load(environment)
        }
        if (config!!.getString("discord.token").isNotEmpty()) {
            apiKey = config!!.getString("discord.token")
        } else if (args.isNotEmpty()) {
            apiKey = args[0]
        } else {
            System.out.println("You need to enter a key")
            exitProcess(0)
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
        //client!!.dispatcher.registerListener(XKCDLinker())

        Runtime.getRuntime().addShutdownHook(Thread {
            fun run() {
                handler!!.savePermissions()
                print("Permissions saved!")
            }
        })
    }
}