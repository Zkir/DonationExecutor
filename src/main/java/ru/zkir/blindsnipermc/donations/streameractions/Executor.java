package ru.zkir.blindsnipermc.donations.streameractions;

import ru.zkir.blindsnipermc.donations.misc.Utils;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.zkir.blindsnipermc.donations.misc.Utils.*;
import static java.lang.Math.round;
import static org.bukkit.Bukkit.getPlayerExact;

public class Executor {

    public static List<String> executionsNamesList = new ArrayList<>(Arrays.asList("ShitToInventory", "Lesch", "DropActiveItem",
            "PowerKick", "ClearLastDeathDrop", "SpawnCreeper", "GiveDiamonds", "GiveStackOfDiamonds", "GiveBread",
            "CallNKVD", "CallStalin", "RandomChange", "TamedBecomesEnemies", "HalfHeart", "BigBoom", "Nekoglai", "SetNight", "SetDay", "GiveIronSet",
            "GiveIronSword", "GiveDiamondSet", "GiveDiamondSword", "SpawnTamedDog", "SpawnTamedCat", "HealPlayer", "GiveIronKirka", "GiveDiamondKirka",
            "KillStalins", "TakeOffBlock"));



    public static void DoExecute(String streamerName, String donationUsername, Double fullDonationAmount, String executionName) {

        Player streamerPlayer = getPlayerExact(streamerName);
        boolean canContinue = true;
        //Определяем игрока (если он оффлайн - не выполняем донат и пишем об этом в консоль), а также определяем мир, местоположение и направление игрока
        if (streamerPlayer == null) {
            canContinue = false;
        } else if (streamerPlayer.isDead()) {
            canContinue = false;
        }

        //Если имя донатера не указано - устанавливаем в качестве имени "Кто-то"
        String validDonationUsername;
        if (donationUsername.isEmpty()) {
            validDonationUsername = "Аноним";
        } else if (!isBlackListed(donationUsername)){
            validDonationUsername = donationUsername;
        } else {
            validDonationUsername = "Донатер";
            assert streamerPlayer != null;
            Utils.logToConsole("§eникнейм донатера §f" + donationUsername + "§e был скрыт, как подозрительный");
            streamerPlayer.sendActionBar("НИКНЕЙМ ДОНАТЕРА БЫЛ СКРЫТ");
        }


        if (!canContinue) {
            logToConsole("Донат от §b" + donationUsername + " §f в размере §b" + fullDonationAmount + "§f не выполнен из-за того, что целевой стример был недоступен.");
            return;
        }

        //Actually we do nothing.
        logToConsole("Молчаливое выполнение "+ executionName +" (Донат от §b" + donationUsername + " §f в размере §b" + fullDonationAmount + "§f) ");
    }


}
