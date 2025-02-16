package ru.zkir.blindsnipermc.donations.donationalertslink.donationalerts;

import ru.zkir.blindsnipermc.donations.donationalertslink.Donation;
import ru.zkir.blindsnipermc.donations.misc.MainConfig;
import ru.zkir.blindsnipermc.donations.misc.Utils;

import ru.zkir.blindsnipermc.donations.streameractions.Executor;
import ru.zkir.blindsnipermc.donations.streameractions.StreamerPlayer;

import org.bukkit.Bukkit;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DonationAlertsToken {
    private DonationAlertsConnection donationAlertsConnection;
    private final List<StreamerPlayer> listOfStreamerPlayers = new ArrayList<>();
    private final String token;

    public DonationAlertsToken(String token) {
        this.token = token;
        try {
            donationAlertsConnection = new DonationAlertsConnection(this);
            donationAlertsConnection.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for (String spName : Objects.requireNonNull(MainConfig.getConfig().getConfigurationSection("donation-amounts." + token)).getKeys(false)) {
            listOfStreamerPlayers.add(new StreamerPlayer(spName, this));
        }
    }

    public String getToken() {
        return token;
    }

    public void executeDonationsInQueues() {
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            if ( (Bukkit.getPlayerExact(sp.getName()) != null) && (!(Objects.requireNonNull(Bukkit.getPlayerExact(sp.getName())).isDead())) ) {
                    Donation donation = sp.takeDonationFromQueue();
                    if (donation==null) {
                        continue;
                    }
                    Utils.logToConsole("Отправлен на выполнение донат §b" + donation.getExecutionName() + "§f для стримера §b" + sp.getName() + "§f от донатера §b" + donation.getName());
                    Executor.DoExecute(sp.getName(), donation.getName(), donation.getAmount(), donation.getExecutionName());
            }

        }
    }

    //Добавление доната в очередь
    public void addToDonationsQueue(Donation donation) {

        String execution;
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            execution = sp.checkExecution(String.valueOf(Math.round(donation.getAmount())));
            if (!(execution == null)) {
                donation.setExecutionName(execution);
                sp.putDonationToQueue(donation);
                return;
            }
        }
    }

    public void disconnect() {
        donationAlertsConnection.disconnect();
    }

    public int getNumberOfStreamerPlayers() {
        return listOfStreamerPlayers.size();
    }
}