package dev.kilgore.bots.discord.tankbot.commands.admin.settings

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import dev.kilgore.bots.discord.tankbot.Main.client
import sx.blah.discord.handle.obj.ActivityType
import sx.blah.discord.handle.obj.StatusType
import sx.blah.discord.util.EmbedBuilder

class ChangeGame : CommandExecutor {
    @Command(aliases = arrayOf("changegame", "chggame"), usage = "changegame <string>", description = "Change the game the bots playing", async = true, requiredPermissions = "admin.settings.game")
    fun onChangeAvatarCommand(args: Array<String>): String {
        val builder = StringBuilder()
        return if (!args.isEmpty()) {
            args.forEach { arg ->
                builder.append("$arg ")
            }
            client!!.changePresence(StatusType.ONLINE, ActivityType.WATCHING, builder.toString())
            "Changed my game!"
        } else {
            "Not enough arguments"
        }
    }
}