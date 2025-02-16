package ru.zkir.blindsnipermc.donations.donationalertslink;

import java.net.URISyntaxException;

public class DonationAlertsToken {
    private DonationAlertsConnection donationAlertsConnection;
    private final String token;

    public DonationAlertsToken(String token) {
        this.token = token;
        try {
            donationAlertsConnection = new DonationAlertsConnection(this);
            donationAlertsConnection.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public String getToken() {
        return token;
    }

    public void disconnect() {
        donationAlertsConnection.disconnect();
    }

}