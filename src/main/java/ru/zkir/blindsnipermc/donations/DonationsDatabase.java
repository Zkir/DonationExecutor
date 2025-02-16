package ru.zkir.blindsnipermc.donations;
import java.sql.*;

public class DonationsDatabase {
    private final Connection connection;

    public DonationsDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement1 = connection.createStatement()) {
            statement1.execute("CREATE TABLE IF NOT EXISTS Donates (" +
                    "donater_uuid  TEXT NOT NULL, " +
                    "donater_name  TEXT NOT NULL, " +
                    "donate_date   TEXT NOT NULL, " +
                    "donate_amount INTEGER " +
                    //"PRIMARY KEY (invitee_name, inviter_name, invite_date)" +
                    " )");
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayerDonate( String donater_uuid, String donater_name, Long amount) throws SQLException {
        String given_date = java.time.Instant.now().toString();
        //this should error if the player donate already exists
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Donates (donater_uuid, donater_name, donate_date, donate_amount ) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, donater_uuid);
            preparedStatement.setString(2, donater_name);
            preparedStatement.setString(3, given_date);
            preparedStatement.setLong(4, amount);
            preparedStatement.executeUpdate();
        }
    }

    public Integer getDonateAmount(String player_name, Integer days ) throws SQLException{
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT SUM(donate_amount) AS amount FROM Donates WHERE donater_name = ? AND (julianday('now') - julianday(donate_date)) < ? ")) {
            preparedStatement.setString(1, player_name);
            preparedStatement.setInt(2, days);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Bukkit.getLogger().severe( preparedStatement.toString());
            if (resultSet.next()){
                return resultSet.getInt("amount");
            }

            return 0;
        }
    }

}
