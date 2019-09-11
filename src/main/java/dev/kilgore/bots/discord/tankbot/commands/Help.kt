package dev.kilgore.bots.discord.tankbot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import dev.kilgore.bots.discord.tankbot.Main.client
import dev.kilgore.bots.discord.tankbot.util.NewHandler
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

class Help(private val commandHandler: NewHandler) : CommandExecutor {
    @Command(aliases = arrayOf("help", "h"), description = "Shows this page", usage = "help", async = true)
    fun onHelpCommand(user: IUser, message: IMessage): String {
        val builder = StringBuilder()
        builder.append("**Commands:**\n")
        builder.append("```xml")
        commandHandler.commands.forEach { command ->
            if (!command.commandAnnotation.showInHelpPage) {
                return@forEach // skip command
            }
            if (!commandHandler.hasPermission(user.stringID, command.commandAnnotation.requiredPermissions) || client!!.applicationOwner.discriminator != user.discriminator) {
                return@forEach
            }
            builder.append("\n")
            if (!command.commandAnnotation.requiresMention) {
                // the default prefix only works if the command does not require a mention
                builder.append(commandHandler.defaultPrefix)
            }
            var usage = command.commandAnnotation.usage
            if (usage.isEmpty()) { // no usage provided, using the first alias
                usage = command.commandAnnotation.aliases[0]
            }
            builder.append(usage)
            val description = command.commandAnnotation.description
            if (description != "none") {
                builder.append(" | ").append(description)
            }
        }
        builder.append("```")
        return builder.toString()
    }
}