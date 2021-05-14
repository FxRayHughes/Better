package ray.mintcat.better.item

import io.izzel.taboolib.module.compat.PlaceholderHook
import io.izzel.taboolib.module.inject.THook
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import ray.mintcat.better.Better


@THook
class ItemPapi : PlaceholderHook.Expansion {

    override fun plugin(): Plugin {
        return Better.plugin
    }

    override fun identifier(): String {
        return "itempapi"
    }


    override fun onPlaceholderRequest(player: Player, params: String): String {
        if (!player.isOnline) {
            return "N/A"
        }
        val param = params.split("_".toRegex())
        return when (param[0]) {
            "has" -> ItemFeed.getItemAmount(player, param[1]).toString()
            else -> "Null"
        }
    }
}
