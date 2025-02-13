package igorlink.donationexecutor.playersmanagement.donationalerts;

import igorlink.donationexecutor.DonationExecutor;
import igorlink.donationexecutor.Executor;
import igorlink.donationexecutor.playersmanagement.Donation;
import igorlink.donationexecutor.playersmanagement.StreamerPlayer;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import java.net.URISyntaxException;
import java.sql.SQLException;
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
                    Utils.logToConsole("Отправлен на выполнение донат §b" + donation.getexecutionName() + "§f для стримера §b" + sp.getName() + "§f от донатера §b" + donation.getName());
                    Executor.DoExecute(sp.getName(), donation.getName(), donation.getAmount(), donation.getexecutionName());
            }

        }
    }

    public void addStreamerPlayer(@NotNull String streamerPlayerName) {
        listOfStreamerPlayers.add(new StreamerPlayer(streamerPlayerName, this));
    }

    public StreamerPlayer getStreamerPlayer(@NotNull String name) {
        for (StreamerPlayer p : listOfStreamerPlayers) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    //Добавление доната в очередь
    public void addToDonationsQueue(Donation donation) {
        DonationExecutor p = (DonationExecutor) Bukkit.getPluginManager().getPlugin("DonationExecutor");

        String donater_name = donation.getName();
        Integer amount = Math.round(Float.valueOf(donation.getAmount()));

        OfflinePlayer donater = Bukkit.getServer().getOfflinePlayer(donater_name);
        //NOTE: it seems that hasPlayedBefore() does NOT work correctly for bedrock players.
        if (donater.hasPlayedBefore() || donater_name.charAt(0) == '.') {
            String donater_uuid = donater.getUniqueId().toString();
            try {
                p.donatesDatabase.addPlayerDonate(donater_uuid, donater_name, amount);
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


        String execution;
        for (StreamerPlayer sp : listOfStreamerPlayers) {
            execution = sp.checkExecution(Utils.cutOffKopeykis(donation.getAmount()));
            if (!(execution == null)) {
                donation.setexecutionName(execution);
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