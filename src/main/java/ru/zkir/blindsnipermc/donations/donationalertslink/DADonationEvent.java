package ru.zkir.blindsnipermc.donations.donationalertslink;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DADonationEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Donation donation;

    public DADonationEvent(Donation donation){
        this.donation = donation;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public Donation getDonation(){
        return this.donation;
    }

}
