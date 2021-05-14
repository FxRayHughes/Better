package ray.mintcat.better.attribute

import io.izzel.taboolib.module.inject.TListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.serverct.ersha.api.AttributeAPI
import org.serverct.ersha.api.event.AttrUpdateAttributeEvent
import ray.mintcat.better.Better
import ray.mintcat.better.util.Helper


@TListener
class AttrListener : Listener, Helper {

    @EventHandler
    fun onAttrAttributeUpdateEvent(event: AttrUpdateAttributeEvent.After) {
        val player = event.attributeData.entity
        if (player !is Player) {
            return
        }
        val data = event.attributeData
        val attributeList = Better.settings.getStringList("WorldAttribute.${player.world.name}") ?: return
        AttributeAPI.getAPI().addSourceAttribute(data, "world", attributeList, false)
    }

    @EventHandler
    fun onGroups(event: AttrUpdateAttributeEvent.After) {
        val player = event.attributeData.entity
        if (player !is Player) {
            return
        }
        val data = event.attributeData
        Better.settings.getConfigurationSection("GroupAttribute").getKeys(false).forEach { group ->
            Better.settings.getConfigurationSection("GroupAttribute.${group}").getKeys(false).forEach key@{ key ->
                val check = Better.settings.getStringList("GroupAttribute.${group}.${key}.check")
                var keys = false
                check(player, check).thenAccept {
                    if (it) {
                        val attributeList = Better.settings.getStringList("GroupAttribute.${group}.${key}.action")
                        AttributeAPI.getAPI().addSourceAttribute(data, "Better-${group}", attributeList, false)
                        keys = true
                    }
                }
                if (keys) {
                    return@key
                }
            }
        }
    }
}