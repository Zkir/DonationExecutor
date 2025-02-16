package igorlink.donationexecutor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.zkir.blindsnipermc.donations.DonationProcessor;
import ru.zkir.blindsnipermc.donations.donationalertslink.DADonationEvent;
import ru.zkir.blindsnipermc.donations.donationalertslink.Donation;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import static ru.zkir.blindsnipermc.donations.misc.Utils.*;

public class GeneralEventListener implements Listener {


    //Закачка ресурспака и оповещение о том, что плагин не активен, если он не активен
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if (!isPluginActive()) {
            sendSysMsgToPlayer(e.getPlayer(), "плагин не активен. Укажите токен и свой никнейм в файле конфигурации плагина и перезапустите сервер.");
        }

        // Processing donation dependent things.
        DonationExecutor p = (DonationExecutor) Bukkit.getPluginManager().getPlugin("DonationExecutor");
        Player player = e.getPlayer();


        Integer amount = -1;
        try {
            amount = p.donationsDatabase.getDonateAmount(player.getName(), 30);

        } catch (SQLException exception) {
            Bukkit.getLogger().severe(exception.getMessage());
        }
        String message = "Ваш баланс донатов за последний месяц " + amount + " руб. ";
        player.sendMessage(message);

        ChatColor color_code;
        String display_name;
        String command = "nick "+ player.getName() +" off";
        //Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);

        if (amount<=0){
            // белый ник
            color_code = ChatColor.RESET;
        } else if (amount <=200) {
            // пурпурный ник
            color_code = ChatColor.DARK_PURPLE;
        } else if (amount <=400) {
            // синий ник
            color_code = ChatColor.BLUE;
        } else  if (amount <=800){
            // красный ник
            color_code = ChatColor.RED;
        } else{
            // Желтый ник
            color_code = ChatColor.GOLD;
        }

        display_name = color_code + player.getName();
        //command = "nick " + player.getName() + " " + display_name;

        command = "pex user " + player.getName() + " prefix " + color_code;
        if (amount>0){
            command += "❖";
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        player.setPlayerListName(display_name);
        //TODO: what is the proper way to reset list name?

    }

    @EventHandler
    public void onDonation(DADonationEvent event) {
       Donation donation = event.getDonation();
       logToConsole("Donation event raised! "+ donation.getName() + " donated " + donation.getAmount() +" RUB" );

       // General processing of donations: recording in db and granting donate currency to player
       DonationProcessor.processDonation(donation);

       // Processing of streamer "executions"
       DonationExecutor.getInstance().streamerActionManager.processDonation(donation);

       //note: significant part of actual processing is done on player join.
    }



}

