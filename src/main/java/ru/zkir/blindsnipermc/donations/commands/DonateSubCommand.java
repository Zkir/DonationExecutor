package ru.zkir.blindsnipermc.donations.commands;

import ru.zkir.blindsnipermc.donations.donationalertslink.DADonationEvent;
import ru.zkir.blindsnipermc.donations.donationalertslink.Donation;
import org.bukkit.command.CommandSender;

public class DonateSubCommand {
    public static void onDonateCommand(CommandSender sender, String[] args) {
        int i;

        //Getting donation's amount
        String donationAmount;
        StringBuilder donationUsername = new StringBuilder();
        StringBuilder donationMessage = new StringBuilder();

        //Getting donation's amount
        donationAmount = args[0];

        //Получаем имя донатера
        for (i = 1; i <= args.length - 1; i++) {
            if (i == 1) {
                donationUsername.append(args[i]);
            } else {
                donationUsername.append(' ');
                donationUsername.append(args[i]);
            }
        }
        Donation donation =  new Donation(sender, donationUsername.toString(), donationAmount+".00", "");

        DADonationEvent donationEvent = new DADonationEvent(donation);
        donationEvent.callEvent();

    }
}
