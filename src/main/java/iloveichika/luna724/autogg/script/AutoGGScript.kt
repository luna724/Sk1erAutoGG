package iloveichika.luna724.autogg.script

import iloveichika.luna724.autogg.AutoGG.Companion.autoGGConfig
import iloveichika.luna724.autogg.AutoGG.Companion.configDir
import iloveichika.luna724.autogg.AutoGG.Companion.mc
import net.minecraft.util.ChatComponentText
import java.io.File

class AutoGGScript {
    companion object {
        private val filepath = File(
            configDir, "autogg.txt"
        )
    }

    init {
        if (!filepath.exists()) {
            filepath.createNewFile()
        }
    }

    private fun print(x: String) {
        println(x)
        if (autoGGConfig.originalGGScriptOutputAsChat) {
            mc.thePlayer.addChatMessage(
                ChatComponentText("§e[§dluna724§r §cAutoGG§e]§r§f §7${x}")
            )
        }
    }

    private fun error(x: String): Boolean {
        if (autoGGConfig.originalGGScriptOutputAsChat) {
            mc.thePlayer.addChatMessage(
                ChatComponentText("§e[§dluna724§r §cAutoGG§e]§r§f §4[ERROR]: ${x}")
            )
        }
        return !autoGGConfig.originalGGScriptIgnoreError
    }

    /**　スクリプトに関するメモ
     * # でコメントアウト
     *
     */

    fun autoGGTriggered() {
        if (!filepath.exists()) {
            filepath.createNewFile()
            return
        }
        // ファイルがあるなら読み込み
        /**
         * スクリプトの動作をすべて保存する
         * !で始まるものはスリープ (ms) 、 ?で始まるものはコマンド 、 # で始まるものは /ac ${SAY} でしゃべる 、@ ではじまるものは /pc でしゃべる
         */
        val scriptToDo: MutableList<String> = mutableListOf()

        val reader = filepath.bufferedReader()
        val lines = reader.readLines()
        var stop: Boolean = false
        for ((i, line) in lines.withIndex()) {
            if (stop) {
                break
            }

            if (line.startsWith("#")) {
                print("Skipped comment: $line")
                continue
            }

            else if (line.startsWith("sleep(") && line.endsWith(")")) {
                val sleepTimeString: String = line.substring(6, line.length - 1)
                var sleepTime: Long? = sleepTimeString.toLongOrNull()
                sleepTime ?: {
                    stop = error("Invalid sleep time in ln$i: $sleepTimeString")
                }
                sleepTime = sleepTime ?: 0
                print("Sleeping for $sleepTime ms")
                if (sleepTime <= 0) {
                    stop = error("Negative sleep time in ln$i: $sleepTimeString")
                    scriptToDo.add("!$sleepTime")
                }
            }

            else if (line.startsWith("/")) {
                print("Sending command: $line")
                scriptToDo.add("?${line.substring(1)}")
            }

            else if (line.startsWith("!say")) {
                print("Sending message in ALL chat: $line")
                scriptToDo.add("#${line.substring(4)}")
            }

            else if (line.startsWith("!party")) {
                print("Sending message in PARTY chat: $line")
                scriptToDo.add("@${line.substring(7)}")
            }

            else {
                stop = error("Invalid line in ln$i: $line")
            }
        }
        reader.close()

        // 実行
        Thread {
            for (cmd in scriptToDo) {
                if (cmd.startsWith("!")) {
                    Thread.sleep(cmd.substring(1).toLong())
                } else if (cmd.startsWith("?")) {
                    mc.thePlayer.sendChatMessage("/${cmd.substring(1)}")
                } else if (cmd.startsWith("#")) {
                    mc.thePlayer.sendChatMessage("/ac ${cmd.substring(1)}")
                } else if (cmd.startsWith("@")) {
                    mc.thePlayer.sendChatMessage("/pc ${cmd.substring(1)}")
                }
            }
        }.start()
    }
}