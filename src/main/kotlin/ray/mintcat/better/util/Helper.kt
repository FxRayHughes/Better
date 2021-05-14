package ray.mintcat.better.util

import io.izzel.taboolib.Version
import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Coerce
import io.izzel.taboolib.util.lite.cooldown.Cooldown
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

interface Helper {

    fun Player.getTargetBlockExact(): Block? {
        return this.getTargetBlock(setOf(Material.AIR), 10)
    }

    fun String.form(): String {
        return "§8[§f KetherxScriptBlock §8] §7${this.replace("&", "§")}"
    }

    fun CommandSender.info(value: String) {
        this.sendMessage("§8[§f KetherxScriptBlock §8] §7${value.replace("&", "§")}")
        if (this is Player && !Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f)
        }
    }

    fun CommandSender.error(value: String) {
        this.sendMessage("§8[§c KetherxScriptBlock §8] §7${value.replace("&", "§")}")
        if (this is Player && !Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
        }
    }

    fun Player.info(value: String) {
        this.sendMessage("§8[§f KetherxScriptBlock §8] §7${value.replace("&", "§")}")
        if (!Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f)
        }
    }

    fun Player.error(value: String) {
        this.sendMessage("§8[§c KetherxScriptBlock §8] §7${value.replace("&", "§")}")
        if (!Global.cd.isCooldown(this.name)) {
            this.playSound(this.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
        }
    }

    fun Block.display() {
        world.playEffect(location, Effect.STEP_SOUND, type)
    }

    fun String.unColored(): String {
        return TLocale.Translate.setUncolored(this)
    }

    object Global {
        @TInject
        val cd = Cooldown("command.sound", 50)
    }

    fun eval(player: Player, action: List<String>) {
        try {
            KetherShell.eval(action) {
                sender = player
            }
        } catch (e: LocalizedException) {
            e.print()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun check(player: Player, condition: List<String>): CompletableFuture<Boolean> {
        return if (condition.isEmpty()) {
            CompletableFuture.completedFuture(true)
        } else {
            try {
                KetherShell.eval(condition) {
                    sender = player
                }.thenApply {
                    Coerce.toBoolean(it)
                }
            } catch (e: LocalizedException) {
                e.print()
                CompletableFuture.completedFuture(false)
            } catch (e: Throwable) {
                e.printStackTrace()
                CompletableFuture.completedFuture(false)
            }
        }
    }

    fun LocalizedException.print() {
        println("[Ketherx] Unexpected exception while parsing kether shell:")
        localizedMessage.split("\n").forEach {
            println("[Ketherx] $it")
        }
    }
}