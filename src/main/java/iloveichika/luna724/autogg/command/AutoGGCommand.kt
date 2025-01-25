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
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender

import java.util.Objects;

class AutoGGCommand : CommandBase() {
    override fun getCommandName() = "lunautogg"
    override fun getCommandAliases() = listOf("gg")
    override fun getCommandUsage(sender: ICommandSender) = "/lunautogg"
    override fun getRequiredPermissionLevel() = 0
    override fun canCommandSenderUseCommand(sender: ICommandSender) = true
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        val subcommand = args.getOrNull(0) ?: ""

        if (subcommand == "refresh") {
            refresh()
        } else {
            GuiUtil.open(Objects.requireNonNull(AutoGG.Companion.autoGGConfig.gui()));
        }
    }

    fun refresh() {
        Multithreading.runAsync(RetrieveTriggersTask());
        EssentialAPI.getMinecraftUtil().sendMessage(UTextComponent(ChatColor.GREEN + "Refreshed triggers!"));
    }
}
