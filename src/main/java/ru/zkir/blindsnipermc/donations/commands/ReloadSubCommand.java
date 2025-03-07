package ru.zkir.blindsnipermc.donations.commands;

import igorlink.donationexecutor.DonationExecutor;
import ru.zkir.blindsnipermc.donations.misc.MainConfig;
import ru.zkir.blindsnipermc.donations.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ReloadSubCommand {
    public static void onReloadCommand(CommandSender sender) throws InterruptedException {
        MainConfig.loadMainConfig(true);
        DonationExecutor.getInstance().daTokenManager.reload();
        Utils.logToConsole("Настройки успешно обновлены!");
        if (sender instanceof Player) {
            Utils.sendSysMsgToPlayer(Objects.requireNonNull(((Player) sender).getPlayer()), "Настройки успешно обновлены!");
        }
    }
}
