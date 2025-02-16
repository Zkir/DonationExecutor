package ru.zkir.blindsnipermc.donations.donationalertslink;

import igorlink.donationexecutor.DonationExecutor;
import ru.zkir.blindsnipermc.donations.misc.MainConfig;
import ru.zkir.blindsnipermc.donations.misc.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DATokenManager {
    private final List<DonationAlertsToken> listOfDonationAlertsTokens = new ArrayList<>();

    public DATokenManager() {
        getTokensFromConfig();
    }

    private void getTokensFromConfig() {
        Set<String> tokensStringList = Objects.requireNonNull(MainConfig.getConfig().getConfigurationSection("donation-amounts")).getKeys(false);
        for (String token : tokensStringList) {
            this.addTokenToList(token);
        }

        int numOfStreamerPlayers = -1; //TODO: fix!!
        //for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            //numOfStreamerPlayers += token.getNumberOfStreamerPlayers();
        //}

        Utils.logToConsole("Было добавлено §b" + listOfDonationAlertsTokens.size() + " §fтокенов, с которыми связано §b" + numOfStreamerPlayers + " §fигроков.");
    }

    private void addTokenToList(String token) {
        listOfDonationAlertsTokens.add(new DonationAlertsToken(token));
    }

    public void reload() throws InterruptedException {
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            token.disconnect();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                listOfDonationAlertsTokens.clear();
                getTokensFromConfig();
            }
        }.runTaskLater(DonationExecutor.getInstance(), 20);

    }

    public void stop() throws InterruptedException {
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            token.disconnect();
        }
        Thread.sleep(1000);
        listOfDonationAlertsTokens.clear();
    }



}
