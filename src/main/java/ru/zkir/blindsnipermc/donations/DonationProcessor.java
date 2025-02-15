package ru.zkir.blindsnipermc.donations;

import igorlink.donationexecutor.DonationExecutor;
import org.jetbrains.annotations.NotNull;
import ru.zkir.blindsnipermc.donations.donationalertslink.Donation;
import ru.zkir.blindsnipermc.donations.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;

public class DonationProcessor {

    //processing of the donations for the purposes of BlindSniperMC plugin
    // 1. donation is recorded in the DB
    // 2. donater is given in-game currency.
    public static void immediateDonationProcessing(@NotNull Donation donation){
        DonationExecutor p = DonationExecutor.getInstance();

        String donater_name = donation.getName();
        Integer amount = Math.round(Float.parseFloat(donation.getAmount()));

        OfflinePlayer donater = Bukkit.getServer().getOfflinePlayer(donater_name);
        //NOTE: it seems that hasPlayedBefore() does NOT work correctly for bedrock players.
        if (donater.hasPlayedBefore() || donater_name.charAt(0) == '.') {
            String donater_uuid = donater.getUniqueId().toString();
            try {
                p.donationsDatabase.addPlayerDonate(donater_uuid, donater_name, amount);
                String command = "eco give " + donater_name +" " + amount*10;
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);

                Utils.logToConsole("Донат от §b" + donation.getName() + "§f в размере §b" + donation.getAmount() + " руб.§f был обработан и отправлен в очередь на выполнение.");
            } catch (SQLException e) {
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        else{
            Utils.logToConsole("Донат от §b" + donation.getName() + "§f в размере §b" + donation.getAmount() + " руб.§f  НЕ обработан. ТАКОЙ ИГРОК НЕ НАЙДЕН ");
        }
    }
}
