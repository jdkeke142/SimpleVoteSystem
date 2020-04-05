package fr.keke142.simplevotesystem.database;

import fr.keke142.simplevotesystem.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VotesNamesTable extends AbstractTable {
    private static final String ADD_OR_UPDATE_NAME = "INSERT INTO ivv_names (votePlayer, votePlayerName) VALUES(?, ?) ON DUPLICATE KEY UPDATE votePlayerName=?";
    private static final String GET_NAME_FROM_UUID = "SELECT votePlayerName FROM ivv_names WHERE votePlayer=?";

    public VotesNamesTable(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public void addOrUpdateName(String votePlayer, String votePlayerName) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement incrementCounts = connection.prepareStatement(ADD_OR_UPDATE_NAME)) {
            incrementCounts.setString(1, votePlayer);
            incrementCounts.setString(2, votePlayerName);
            incrementCounts.setString(3, votePlayerName);

            incrementCounts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNameFromUuid(String votePlayer) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement getNameFromUuid = connection.prepareStatement(GET_NAME_FROM_UUID)) {
            getNameFromUuid.setString(1, votePlayer);

            ResultSet res = getNameFromUuid.executeQuery();
            if (res.next()) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String[] getTable() {
        return new String[]{"ivv_names", "`votePlayer` VARCHAR(36) NOT NULL, " +
                "`votePlayerName` VARCHAR(16) NOT NULL," +
                "PRIMARY KEY (`votePlayer`)",
                "ENGINE=MyISAM DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci"};
    }
}
