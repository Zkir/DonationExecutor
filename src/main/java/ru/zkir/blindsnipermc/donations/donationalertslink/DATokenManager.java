package ru.zkir.blindsnipermc.donations.donationalertslink;

import ru.zkir.blindsnipermc.donations.donationalertslink.donationalerts.DonationAlertsToken;
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

    //Таймер будет выполнять донаты из очередей игроков каждые 2 сек, если они живы и онлайн - выполняем донат и убираем его из очереди
    public DATokenManager() {
        getTokensFromConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!DonationExecutor.isRunning()) {
                    this.cancel();
                    return;
                }

                for (DonationAlertsToken token : listOfDonationAlertsTokens) {
                    token.executeDonationsInQueues();
                }
            }
        }.runTaskTimer(DonationExecutor.getInstance(), 0, 40);
    }

    private void getTokensFromConfig() {
        Set<String> tokensStringList = Objects.requireNonNull(MainConfig.getConfig().getConfigurationSection("donation-amounts")).getKeys(false);
        for (String token : tokensStringList) {
            this.addTokenToList(token);
        }

        int numOfStreamerPlayers = 0;
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            numOfStreamerPlayers += token.getNumberOfStreamerPlayers();
        }

        Utils.logToConsole("Было добавлено §b" + listOfDonationAlertsTokens.size() + " §fтокенов, с которыми связано §b" + numOfStreamerPlayers + " §fигроков.");
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

    public void addToDonationsQueue(Donation donation) {
        //this method is activated from console, not from web-socket!!
        //so in this case there is no token, so donation is added for ALL tokens and ALL streamer players.
        for (DonationAlertsToken token : listOfDonationAlertsTokens) {
            token.addToDonationsQueue(donation);
        }
    }

    private void addTokenToList(String token) {
        listOfDonationAlertsTokens.add(new DonationAlertsToken(token));
    }

}
