package dev.kilgore.bots.discord.tankbot.commands.admin

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import dev.kilgore.bots.discord.tankbot.Main.client
import dev.kilgore.bots.discord.tankbot.Main.handler
import sx.blah.discord.handle.obj.IUser
import kotlin.system.exitProcess

class Exit : CommandExecutor {
    @Command(aliases = arrayOf("exit", "logout"), description = "Logout and exit the bot", usage = "exit", channelMessages = false)
    fun onExitCommand(user: IUser): String {
        if (user.discriminator == client!!.applicationOwner.discriminator || handler!!.hasPermission(user.stringID, "admin.exit")) {
            user.orCreatePMChannel.sendMessage("Bot shutting down!")
            exitProgram()
            return "Bot shutting down!"
        } else {
            return "You can't do that!"
        }
    }

    private fun exitProgram() {
        handler!!.savePermissions()
        Thread.sleep(5000)
        client!!.logout()
        exitProcess(0)
    }
}