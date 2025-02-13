package igorlink.donationexecutor;

import igorlink.command.DonationExecutorCommand;
import igorlink.donationexecutor.executionsstaff.executionsmanagement.executions.inventory.ShitToInventory;
import igorlink.donationexecutor.executionsstaff.giantmobs.GiantMobManager;
import igorlink.donationexecutor.playersmanagement.StreamerPlayersManager;
import igorlink.service.MainConfig;
import igorlink.service.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static igorlink.service.Utils.*;

import ru.zkir.blindsnipermc.donates.DonatesDatabase;
import java.sql.SQLException;

public final class DonationExecutor extends JavaPlugin {
    private static DonationExecutor instance;
    public static GiantMobManager giantMobManager;
    private static Boolean isRunningStatus = true;
    public StreamerPlayersManager streamerPlayersManager;

    public DonatesDatabase donatesDatabase;


    @Override
    public void onEnable() {
        instance = this;
        try {
            MainConfig.loadMainConfig();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (CheckNameAndToken()) {
            streamerPlayersManager = new StreamerPlayersManager();
            giantMobManager = new GiantMobManager(this);
            new DonationExecutorCommand();
            Utils.fillTheSynonimousCharsHashMap();
        }

        Bukkit.getPluginManager().registerEvents(new GeneralEventListener(),this);

        try {
            this.donatesDatabase = new DonatesDatabase(getDataFolder().getAbsolutePath() + "/donations.db");
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
                streamerPlayersManager.stop();
            }
        } catch (InterruptedException e) {
            logToConsole("Какая-то ебаная ошибка, похуй на нее вообще");
        }

        try {
            donatesDatabase.closeConnection();
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
