package ray.mintcat.better.item

import github.saukiya.sxattribute.SXAttribute
import io.izzel.taboolib.util.item.Items
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ItemFeed {

    fun getItemStack(player: Player?, name: String): ItemStack? {
        // String format: MC/MM: ItemsName/ItemMate
        val type: Array<String?> = name.split(":").toTypedArray()
        val itemStack = ItemStack(Material.AIR)
        return when (type[0]) {
            "MM" -> MythicMobs.inst().itemManager.getItemStack(type[1])
            "MC" -> {
                itemStack.type = Material.valueOf(type[1]!!)
                itemStack
            }
            "SX" -> SXAttribute.getApi().getItem(type[1], player)
            "IJ" -> Items.fromJson(type[1])
            else -> itemStack
        }
    }

    fun getItemAmount(player: Player, name: String): Int {
        val itemStack = getItemStack(player, name) ?: return 0
        var i = 0
        Items.takeItem(player.inventory, {
            if (it.isSimilar(itemStack)) {
                i += it.amount
            }
            false
        }, 999)
        return i
    }

    fun giveItemStack(item: String, player: Player, amount: Int): Boolean {
        for (a in 1..amount) {
            val itemStack = getItemStack(player, item) ?: return false
            player.inventory.addItem(itemStack)
        }
        return true
    }


    fun takeItem(player: Player, name: String, amount: Int): Boolean {
        val itemStack = getItemStack(player, name)
        val hasAmount = getItemAmount(player, name)
        if (hasAmount > amount) {
            Items.checkItem(player, itemStack, amount, true)
            return true
        }
        return false
    }
}