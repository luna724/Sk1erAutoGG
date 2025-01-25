package iloveichika.luna724.autogg.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import iloveichika.luna724.autogg.AutoGG.Companion.configDir

import java.io.File;

@SuppressWarnings("FieldMayBeFinal")
class AutoGGConfig : Vigilant(File(configDir, "luna724_autogg.toml")) {
    @Property(
            type = PropertyType.SWITCH, name = "AutoGG",
            description = "Entirely toggles AutoGG",
            category = "General", subcategory = "General"
    )
    var autoGGEnabled: Boolean = true

    @Property(
        type = PropertyType.SWITCH, name = "Prevent GG Spam",
        description = "Prevent GG spamming in chat. \n(set GG Message cooldown to 10s)",
        category = "General", subcategory = "General"
    )
    var preventGGSpam: Boolean = false

    @Property(
            type = PropertyType.SWITCH, name = "Casual AutoGG",
            description = "Enable AutoGG for things that don't give Karma such as Skyblock Events.",
            category = "General", subcategory = "General"
    )
    var casualAutoGGEnabled: Boolean = false

    @Property(
            type = PropertyType.SWITCH, name = "Anti GG",
            description = "Remove priory GG messages from chat.",
            category = "General", subcategory = "Miscellaneous"
    )
    var antiGGEnabled: Boolean = false

    @Property(
            type = PropertyType.SWITCH, name = "Anti Karma",
            description = "Remove Karma messages from chat.",
            category = "General", subcategory = "Miscellaneous"
    )
    var antiKarmaEnabled: Boolean = false

    @Property(
            type = PropertyType.SLIDER, name = "Delay",
            description = "Delay after the game ends to say the message.\n§eMeasured in seconds.",
            category = "General", subcategory = "General",
            min = 0, max = 5, increment = 1
    )
    var autoGGDelay: Int = 0

    @Property(
            type = PropertyType.TEXT, name = "Phrase",
            description = "Choose what message is said on game completion.",
            category = "General", subcategory = "General"
    )
    var autoGGMessage: String = "gg"

    @Property(
            type = PropertyType.SWITCH, name = "Second Message",
            description = "Enable a secondary message to send after your first GG.",
            category = "General", subcategory = "Secondary Message"
    )
    var secondaryEnabled: Boolean = false

    @Property(
            type = PropertyType.TEXT, name = "Phrase",
            description = "Send a secondary message sent after the first GG message.",
            category = "General", subcategory = "Secondary Message"
    )
    var autoGGMessage2: String = ""

    @Property(
            type = PropertyType.SLIDER, name = "Second Message Delay",
            description = "Delay between the first & second end of game messages.\n§eMeasured in seconds.",
            category = "General", subcategory = "Secondary Message",
            max = 5
    )
    var secondaryDelay: Int = 1

    @Property(
            type = PropertyType.SWITCH, name = "AutoQueue",
            description = "Automaticcally send command after sent ALL GG message",
            category = "General", subcategory = "AutoQueue"
    )
    var autoQueue: Boolean = false

    @Property(
            type = PropertyType.TEXT, name = "AutoQueue Command",
            description = "Command for AutoQueue",
            category = "General", subcategory = "AutoQueue"
    )
    var autoQueueCommand: String = "/play bedwars_four_four"

    @Property(
        type = PropertyType.SWITCH, name = "YourOriginalGG Mode",
        description = "Enable YourOriginalGG Mode\n§eif enable this, disable main AutoGG automatically!",
        category = "GGScript", subcategory = "General"
    )
    var yourOriginalGGMode: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "Script output",
        description = "show script internal log to chat",
        category = "GGScript", subcategory = "General"
    )
    var originalGGScriptOutputAsChat: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "ignore Error",
        description = "if occurred error in script, stop script or ignore error\n(enable to ignore error)",
        category = "GGScript", subcategory = "General"
    )
    var originalGGScriptIgnoreError: Boolean = false

    init {
        initialize();
    }
}
