package ru.zkir.blindsnipermc.donations.donationalertslink;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Donation {
    private CommandSender sender;
    private String username;
    private Double amount;

    private String executionName = null;

    public Donation(String _username, String _amount) {
        new Donation(Bukkit.getConsoleSender(), _username, _amount);
    }

    public Donation(CommandSender _sender, String _username, String _amount) {
        sender = _sender;
        if (_username.isEmpty()) {
            username = "Аноним";
        } else {
            username = _username;
        }
        amount = Double.parseDouble(_amount);
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getName() {
        return username;
    }

    public Double getAmount() {
        return amount;
    }

    public String getExecutionName() {
        return executionName;
    }

    public void setExecutionName(String _executionName) {
        executionName = _executionName;
    }


}
