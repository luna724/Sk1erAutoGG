package iloveichika.luna724.autogg

import gg.essential.api.EssentialAPI
import gg.essential.api.utils.WebUtil.fetchJSON
import net.minecraft.client.Minecraft

class Init {
    companion object {
        private var usingEnglish = false

    }

    // preInit 時に非同期で初期化される
    init {
        checkUserLanguage()

    }

    // Hypixelの設定を Sk1er API で確認
    private fun checkUserLanguage() {
        val username = Minecraft.getMinecraft().getSession().getUsername()
        val json = fetchJSON("https://api.sk1er.club/player/$username")
        val language = json.optJSONObject("player").defaultOptString("userLanguage", "ENGLISH")
        usingEnglish = "ENGLISH" == language
    }

    // 英語以外は非対応らしい
    fun languageWarning() {
        if (!usingEnglish) {
            EssentialAPI.getNotifications().push(
                "AutoGG",
                "We've detected your Hypixel language isn't set to English! AutoGG will not work on other languages.\n" +
                        "If this is a mistake, feel free to ignore it.", 6f
            )
        }
    }
}