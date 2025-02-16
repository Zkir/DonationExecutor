package ru.zkir.blindsnipermc.donations.streameractions;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.zkir.blindsnipermc.donations.donationalertslink.Donation;

import ru.zkir.blindsnipermc.donations.misc.MainConfig;
import ru.zkir.blindsnipermc.donations.misc.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StreamerActionManager {
    private final List<StreamerPlayer> listOfStreamerPlayers = new ArrayList<>();

    public StreamerActionManager(){

        Set<String> tokensStringList = Objects.requireNonNull(MainConfig.getConfig().getConfigurationSection("donation-amounts")).getKeys(false);
        for (String token : tokensStringList) {
            for (String spName : Objects.requireNonNull(MainConfig.getConfig().getConfigurationSection("donation-amounts." + token)).getKeys(false)) {
                listOfStreamerPlayers.add(new StreamerPlayer(spName, token));
            }
        }

        //Таймер будет выполнять донаты из очередей игроков каждые 2 сек, если они живы и онлайн - выполняем донат и убираем его из очереди
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!DonationExecutor.isRunning()) {
                    this.cancel();
                    return;
                }
                executeDonationsInQueues();
            }
        }.runTaskTimer(DonationExecutor.getInstance(), 0, 40);
    }

    public void processDonation(Donation donation){
        //Ставим донат в очередь на исполнение

        //when donation is activated from console, not from real DA web-socket,
        //it has no token, so in such a case donation is added to all queues of all players
        String execution;
        for (StreamerPlayer sp : listOfStreamerPlayers){
            if(sp.getDaToken().equals(donation.getDaToken()) || donation.getDaToken().isEmpty()){
                execution = sp.checkExecution(String.valueOf(Math.round(donation.getAmount())));
                if (!(execution == null)) {
                    donation.setExecutionName(execution);
                    sp.putDonationToQueue(donation);
                }
            }
        }
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


}
