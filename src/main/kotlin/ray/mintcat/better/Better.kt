package ray.mintcat.better

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject

object Better: Plugin() {

    @TInject("settings.yml")
    lateinit var settings: TConfig
        private set

}