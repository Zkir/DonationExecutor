package igorlink.donationexecutor;

import ru.zkir.blindsnipermc.donations.commands.DonationExecutorCommand;
import ru.zkir.blindsnipermc.donations.donationalertslink.DATokenManager;
import ru.zkir.blindsnipermc.donations.misc.MainConfig;
import ru.zkir.blindsnipermc.donations.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static ru.zkir.blindsnipermc.donations.misc.Utils.*;

import ru.zkir.blindsnipermc.donations.DonationsDatabase;
import java.sql.SQLException;

public final class DonationExecutor extends JavaPlugin {
    private static DonationExecutor instance;
    private static Boolean isRunningStatus = true;
    public DATokenManager daTokenManager;

    public DonationsDatabase donationsDatabase;


    @Override
    public void onEnable() {
        instance = this;
        try {
            MainConfig.loadMainConfig();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (CheckNameAndToken()) {
            daTokenManager = new DATokenManager();
            new DonationExecutorCommand();
            Utils.fillTheSynonimousCharsHashMap();
        }

        Bukkit.getPluginManager().registerEvents(new GeneralEventListener(),this);

        try {
            this.donationsDatabase = new DonationsDatabase(getDataFolder().getAbsolutePath() + "/donations.db");
        }
        catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to connect to donation database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }


    }

    @Override
    public void onDisable() {
        try {
            isRunningStatus = false;
            if (CheckNameAndToken()) {
                daTokenManager.stop();
            }
        } catch (InterruptedException e) {
            logToConsole("Какая-то ебаная ошибка, похуй на нее вообще");
        }

        try {
            donationsDatabase.closeConnection();
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }


    }

    public static DonationExecutor getInstance() {
        return instance;
    }


    public static Boolean isRunning() {
        return isRunningStatus;
    }


}
