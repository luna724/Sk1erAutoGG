package iloveichika.luna724.autogg

import gg.essential.api.utils.Multithreading.runAsync
import gg.essential.api.utils.Multithreading.schedule
import iloveichika.luna724.autogg.AutoGG.Companion.autoGGConfig
import iloveichika.luna724.autogg.AutoGG.Companion.autoGGScript
import iloveichika.luna724.autogg.AutoGG.Companion.triggers
import iloveichika.luna724.autogg.handlers.patterns.PatternHandler
import iloveichika.luna724.autogg.tasks.data.Server
import iloveichika.luna724.autogg.tasks.data.TriggerType
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.TimeUnit

/**
 * Where the magic happens...
 * We handle which server's triggers should be used
 * and how to detect which server the player is currently
 * on.
 *
 * @author ChachyDev
 */
class AutoGGHandler {
    @Volatile
    private var server: Server? = null
    private var lastGG: Long = 0

    // ワールド毎に server 変数を設定し、GG タイプを検知
    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        if (!autoGGConfig.autoGGEnabled) return
        if (event.entity === Minecraft.getMinecraft().thePlayer) {
            runAsync(Runnable {
                for (s in triggers!!.servers) {
                    try {
                        // ハンドラーにサーバーがどんなか検出させ、Trueの場合はその値を使用する
                        if (s.getDetectionHandler().detector.detect(s.data)) {
                            server = s
                            return@Runnable
                        }
                    } catch (_: Throwable) {
                        // Stop log spam
                    }
                }
                // In case if it's not null, and we couldn't find the triggers for the current server.
                server = null
            })
        }
    }

    @SubscribeEvent
    fun onClientChatReceived(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2) return
        if (!autoGGConfig.autoGGEnabled) return
        server = server ?: return // server の null を許容しない
        val stripped = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.unformattedText)

        // AntiGG / AntiKarma feature
        for (trigger in server!!.triggers) {
            val triggerType = trigger.type
            if (triggerType == TriggerType.ANTI_GG && autoGGConfig.antiGGEnabled) {
                if (PatternHandler.INSTANCE.getOrRegisterPattern(trigger.pattern).matcher(stripped)
                        .matches()
                ) { // AntiGGメッセージなら非表示に設定
                    event.setCanceled(true)
                    return
                }
            }
            else if (triggerType == TriggerType.ANTI_KARMA && autoGGConfig.antiKarmaEnabled) {
                if (PatternHandler.INSTANCE.getOrRegisterPattern(trigger.pattern).matcher(stripped)
                        .matches()
                ) { // KARMAメッセージなら非表示に設定
                    event.setCanceled(true)
                    return
                }
            }
        }
        // end

        // AutoGG / AutoCasualGG feature
        // 非同期で処理するため Anti系とは分ける。なんでかは知らん
        runAsync(Runnable {
            // Normal タイプまたは Casual で CasualGG が有効なら、invokeGG を実行
            for (trigger in server!!.triggers) {
                val triggerType = trigger.getType()
                if (triggerType == TriggerType.NORMAL ||
                    (triggerType == TriggerType.CASUAL && autoGGConfig.casualAutoGGEnabled)
                ) {
                    if (PatternHandler.INSTANCE.getOrRegisterPattern(trigger.pattern)
                            .matcher(stripped).matches()
                    ) {
                        invokeGG()
                        return@Runnable
                    }
                }
            }
        })
    }

    // GG を送信する
    private fun invokeGG() {
        server = server ?: return // server の null を許容しない

        // Scriptを検出
        if (autoGGConfig.yourOriginalGGMode) {
            autoGGScript.autoGGTriggered()
            return
        }

        // AutoGG が無効に設定されている場合は server が必ず null になるが、念のため配置
        if (!autoGGConfig.autoGGEnabled) return
        if (System.currentTimeMillis() - lastGG < 10000 && autoGGConfig.preventGGSpam) return // クールダウン
        lastGG = System.currentTimeMillis()

        val prefix = server!!.messagePrefix
        val ggMessage = autoGGConfig.autoGGMessage
        val delay = autoGGConfig.autoGGDelay
        if (!ggMessage.isEmpty()) {
            schedule( // 指定した秒数後にメッセージ送る
                Runnable { Minecraft.getMinecraft().thePlayer.sendChatMessage(if (prefix.isEmpty()) ggMessage else "$prefix $ggMessage") },
                delay.toLong(),
                TimeUnit.SECONDS
            )
        }

        if (autoGGConfig.secondaryEnabled) {
            val secondGGMessage = autoGGConfig.autoGGMessage2
            val secondaryDelay =
                autoGGConfig.secondaryDelay +
                        autoGGConfig.autoGGDelay

            if (!secondGGMessage.isEmpty()) {
                schedule(
                    Runnable { Minecraft.getMinecraft().thePlayer.sendChatMessage(if (prefix.isEmpty()) ggMessage else "$prefix $secondGGMessage") },
                    secondaryDelay.toLong(),
                    TimeUnit.SECONDS
                )
            }
        }

        if (autoGGConfig.autoQueue) {
            val autoQueueCommand = autoGGConfig.autoQueueCommand
            val thirdDelay = (autoGGConfig.autoGGDelay + autoGGConfig.secondaryDelay * 1000) + 500
            if (!autoQueueCommand.isEmpty()) {
                schedule(
                    Runnable { Minecraft.getMinecraft().thePlayer.sendChatMessage(autoQueueCommand) },
                    thirdDelay.toLong(),
                    TimeUnit.MILLISECONDS
                )
            }
        }
    }
}