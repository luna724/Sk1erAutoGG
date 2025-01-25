package iloveichika.luna724.autogg.command;

import iloveichika.luna724.autogg.AutoGG;
import iloveichika.luna724.autogg.tasks.RetrieveTriggersTask;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.commands.SubCommand;
import gg.essential.api.utils.GuiUtil;
import gg.essential.api.utils.Multithreading;
import gg.essential.universal.ChatColor;
import gg.essential.universal.wrappers.message.UTextComponent;

import java.util.Objects;

public class AutoGGCommand extends Command {
    public AutoGGCommand() {
        super("lunautogg");
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(Objects.requireNonNull(AutoGG.INSTANCE.getAutoGGConfig().gui()));
    }

    @SubCommand(value = "refresh", description = "Refreshes your loaded triggers.")
    public void refresh() {
        Multithreading.runAsync(new RetrieveTriggersTask());
        EssentialAPI.getMinecraftUtil().sendMessage(new UTextComponent(ChatColor.GREEN + "Refreshed triggers!"));
    }
}
