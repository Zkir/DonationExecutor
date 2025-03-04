package ru.zkir.blindsnipermc.donations.commands;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand implements CommandExecutor {

    public AbstractCommand(String command) {
        PluginCommand pluginCommand = DonationExecutor.getInstance().getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    public abstract Boolean execute(CommandSender sender, String label, String[] args);


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return execute(sender, label, args);
    }
}
