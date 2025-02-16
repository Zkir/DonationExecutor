package ru.zkir.blindsnipermc.donations.streameractions;

import igorlink.donationexecutor.DonationExecutor;
import ru.zkir.blindsnipermc.donations.donationalertslink.Donation;

public class StreamerActionManager {

    public static void processDonation(Donation donation){
        //Ставим донат в очередь на исполнение
        DonationExecutor.getInstance().daTokenManager.addToDonationsQueue(donation);

    }
}
