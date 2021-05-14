package ray.mintcat.better.attribute

import io.izzel.taboolib.module.command.base.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.serverct.ersha.api.AttributeAPI
import ray.mintcat.better.Better
import ray.mintcat.better.util.Helper
import java.util.*

@BaseCommand(name = "attributions", aliases = ["es", "acc"], permission = "*")
class AttributeCommand : BaseMainCommand(), Helper {

    @SubCommand(description = "临时属性 [可叠加]")
    var separate: BaseSubCommand = object : BaseSubCommand() {
        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"), Argument("属性"), Argument("数值"), Argument("持续时间"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0]) ?: return
            val attributeList: List<String> = Arrays.asList("${args[1]}: ${args[2]}")
            val data = AttributeAPI.getAttrData(player)

            val uuid = UUID.randomUUID().toString()
            AttributeAPI.getAPI().addSourceAttribute(data, uuid, attributeList, false)
            Bukkit.getScheduler().runTaskLater(Better.plugin, {
                AttributeAPI.getAPI().takeSourceAttribute(data, uuid)
            }, args[3].toLong())
        }
    }

    @SubCommand(description = "临时属性 [不可叠加]")
    var public: BaseSubCommand = object : BaseSubCommand() {
        override fun getArguments(): Array<Argument> {
            return arrayOf(Argument("目标"), Argument("属性"), Argument("数值"), Argument("持续时间"), Argument("源"))
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0]) ?: return
            val attributeList: List<String> = Arrays.asList("${args[1]}: ${args[2]}")
            val data = AttributeAPI.getAttrData(player)

            val uuid = args[4]
            AttributeAPI.getAPI().addSourceAttribute(data, uuid, attributeList, false)
            Bukkit.getScheduler().runTaskLater(Better.plugin, {
                AttributeAPI.getAPI().takeSourceAttribute(data, uuid)
            }, args[3].toLong())
        }
    }
}