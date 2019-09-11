package dev.kilgore.bots.discord.tankbot.commands.admin

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import dev.kilgore.bots.discord.tankbot.Main
import dev.kilgore.bots.discord.tankbot.util.NewHandler
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

class AddPermission(private val commandHandler: NewHandler) : CommandExecutor {
    @Command(aliases = arrayOf("addpermission", "addperm"), usage = "addperm <permission> <@user>", description = "Adds permission to user", channelMessages = true, privateMessages = false)
    fun onAddPermissionCommand(args: Array<String>, user: IUser, message: IMessage): String {
        if (user.discriminator == Main.client!!.applicationOwner.discriminator || commandHandler.hasPermission(user.stringID, "admin.permission.add")) {
            if (args.isEmpty() || args.size < 2) {
                return "Not enough arguments"
            } else {
                val users = message.mentions
                users.forEach { user ->
                    if (commandHandler.hasPermission(user.stringID, args[0])) {
                        return user.mention() + " already has the " + args[0] + " permission."
                    }
                    commandHandler.addPermission(user.stringID, args[0])
                    commandHandler.savePermissions()
                    return user.mention() + " has received the " + args[0] + " permission."
                }
                return "This should never happen!"
            }
        } else return user.mention() + " | You do not have enough permissions to do that!"
    }
}