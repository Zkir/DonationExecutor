package igorlink.donationexecutor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.zkir.blindsnipermc.donations.misc.MainConfig;
import ru.zkir.blindsnipermc.donations.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.sql.SQLException;
import static ru.zkir.blindsnipermc.donations.misc.Utils.*;

public class GeneralEventListener implements Listener {


    //Закачка ресурспака и оповещение о том, что плагин не активен, если он не активен
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

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
        String displayname = player.getName();
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

        displayname = color_code + player.getName();
        //command = "nick " + player.getName() + " " + displayname;

        command = "pex user " + player.getName() + " prefix " + color_code;
        if (amount>0){
            command += "❖";
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        player.setPlayerListName(displayname);
        //TODO: what is the proper way to reset list name?

    }


}

