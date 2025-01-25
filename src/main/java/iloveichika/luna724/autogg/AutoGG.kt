/*
 * AutoGG - Automatically say a selectable phrase at the end of a game on supported servers.
 * Copyright (C) 2020  Sk1er LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package iloveichika.luna724.autogg

import gg.essential.api.utils.Multithreading.runAsync
import iloveichika.luna724.autogg.command.AutoGGCommand
import iloveichika.luna724.autogg.config.AutoGGConfig
import iloveichika.luna724.autogg.handlers.patterns.PlaceholderAPI
import iloveichika.luna724.autogg.script.AutoGGScript
import iloveichika.luna724.autogg.tasks.RetrieveTriggersTask
import iloveichika.luna724.autogg.tasks.data.TriggersSchema
import net.minecraft.client.Minecraft
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File

/**
 * Contains the main class for AutoGG which handles trigger schema setting/getting and the main initialization code.
 *
 * @author ChachyDev
 */
@Mod(modid = "lunautogg", name = "luna724AutoGG", version = "1.0")
class AutoGG {
    // おそらく AntiGG Message に使われる
    val primaryGGStrings: List<String> = listOf("gg", "GG", "gf", "Good Game", "Good Fight", "Good Round! :D")
    val secondaryGGStrings: List<String> = listOf(
        "Have a good day!",
        "<3",
        "AutoGG By Sk1er!",
        "gf",
        "Good Fight",
        "Good Round",
        ":D",
        "Well played!",
        "wp"
    )

    @Mod.EventHandler
    fun onFMLPreInitialization(event: FMLPreInitializationEvent) {
        configDir = File(event.modConfigurationDirectory, "luna724_autogg")
        autoGGScript = AutoGGScript()

        runAsync(Runnable {
            initializer = Init()
        })
    }

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        autoGGConfig = AutoGGConfig()
        autoGGConfig.preload()

        // AntiGG Message をなんかしらに保存
        val joined: List<String> = primaryGGStrings + secondaryGGStrings
        PlaceholderAPI.INSTANCE.registerPlaceHolder("antigg_strings", joined.joinToString("|"))

        runAsync(RetrieveTriggersTask())
        MinecraftForge.EVENT_BUS.register(AutoGGHandler())
        ClientCommandHandler.instance.registerCommand(AutoGGCommand())

        // 待機時間が設定可能な値から外れている場合にリセット
        if (autoGGConfig.autoGGDelay > 5) autoGGConfig.autoGGDelay = 1
        if (autoGGConfig.secondaryDelay > 5) autoGGConfig.secondaryDelay = 1
    }

    @Mod.EventHandler
    fun onLoadComplete(event: FMLLoadCompleteEvent) {
        initializer.languageWarning()
    }

    companion object {
        @Mod.Instance
        lateinit var INSTANCE: AutoGG
        lateinit var autoGGConfig: AutoGGConfig
        lateinit var autoGGScript: AutoGGScript
        var triggers: TriggersSchema? = null // なにこれ
        private lateinit var initializer: Init
        lateinit var configDir: File

        val mc = Minecraft.getMinecraft()
    }

    // Java互換の関数
    @Deprecated("use autoGGConfig directly if kotlin!", ReplaceWith("autoGGConfig"), DeprecationLevel.WARNING)
    fun getAutoGGConfig(): AutoGGConfig {
        return autoGGConfig
    }

    @Deprecated("use triggers directly if kotlin!", ReplaceWith("triggers"), DeprecationLevel.WARNING)
    fun setTriggers(newTriggers: TriggersSchema) {
        triggers = newTriggers
    }

    @Deprecated("use triggers directly if kotlin!", ReplaceWith("triggers"), DeprecationLevel.WARNING)
    fun getTriggers(): TriggersSchema {
        return triggers ?: throw IllegalArgumentException("Unknown Exception: triggers is still null but called.")
    }
}