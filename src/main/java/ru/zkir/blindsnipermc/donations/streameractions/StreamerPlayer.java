package ru.zkir.blindsnipermc.donations.streameractions;

import ru.zkir.blindsnipermc.donations.donationalertslink.Donation;
import ru.zkir.blindsnipermc.donations.donationalertslink.donationalerts.DonationAlertsToken;
import ru.zkir.blindsnipermc.donations.misc.MainConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class StreamerPlayer {
    private final String streamerPlayerName;
    private final Queue<Donation> listOfQueuedDonations = new LinkedList<>();
    private final HashMap<String, String> listOfAmounts = new HashMap<>();


    //Инициализация нового объекта стримера-игрока
    public StreamerPlayer(@NotNull String _streamerPlayerName, DonationAlertsToken donationAlertsToken) {
        FileConfiguration config = MainConfig.getConfig();
        streamerPlayerName = _streamerPlayerName;

        //Заполняем список сумм для донатов
        String amount;
        for (String execName : Executor.executionsNamesList) {
            amount = config.getString("donation-amounts." + donationAlertsToken.getToken() + "." + streamerPlayerName + "." + execName);
            if (!(amount==null)) {
                listOfAmounts.put(amount, execName);
            } else {
                //Utils.logToConsole("Сумма доната, необходимая для " + execName + " для стримера " + streamerPlayerName + " не найдена. Проверьте правильность заполнения файла конфигурации DonationExecutor.yml в папке с именем плагина.");
            }
        }
    }

    public String checkExecution(@NotNull String amount) {
        return listOfAmounts.get(amount);
    }

    public String getName(){
        return streamerPlayerName;
    }

    //Работа с очередью донатов
    //Поставить донат в очередь на выполнение донатов для этого игрока
    public void putDonationToQueue(@NotNull Donation donation){
        listOfQueuedDonations.add(donation);
    }

    //Взять донат из очереди и убрать его из нее
    public Donation takeDonationFromQueue(){
        return listOfQueuedDonations.poll();
    }

}
