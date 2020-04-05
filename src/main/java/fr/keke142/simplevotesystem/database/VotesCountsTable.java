package fr.keke142.simplevotesystem.database;

import fr.keke142.simplevotesystem.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class VotesCountsTable extends AbstractTable {
    private static final String GET_COUNTS = "SELECT votesCounts FROM ivv_counts WHERE votePlayer=?";
    private static final String GET_ALL_COUNTS = "SELECT * FROM ivv_counts ORDER BY votesCounts DESC";
    private static final String INCREMENT_COUNTS = "INSERT INTO ivv_counts (votePlayer, votesCounts) VALUES(?, ?) ON DUPLICATE KEY UPDATE votesCounts=votesCounts+1";
    private static final String RESET_ALL_COUNTS = "TRUNCATE TABLE ivv_counts";

    public VotesCountsTable(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public void incrementCounts(String votePlayer) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement incrementCounts = connection.prepareStatement(INCREMENT_COUNTS)) {
            incrementCounts.setString(1, votePlayer);
            incrementCounts.setInt(2, 1);


            incrementCounts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetAllCounts() {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement resetAllCounts = connection.prepareStatement(RESET_ALL_COUNTS)) {

            resetAllCounts.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCounts(String votePlayer) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement getCounts = connection.prepareStatement(GET_COUNTS)) {

            getCounts.setString(1, votePlayer);

            ResultSet res = getCounts.executeQuery();
            if (res.next()) {

                return res.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public LinkedHashMap<String, Integer> getAllCounts() {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement getAllCounts = connection.prepareStatement(GET_ALL_COUNTS)) {

            ResultSet res = getAllCounts.executeQuery();

            LinkedHashMap<String, Integer> counts = new LinkedHashMap<>();
            while (res.next()) {
                counts.put(res.getString(1), res.getInt(2));
            }

            return counts;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String[] getTable() {
        return new String[]{"ivv_counts", "`votePlayer` VARCHAR(36) NOT NULL, " +
                "`votesCounts` INT NOT NULL," +
                "PRIMARY KEY (`votePlayer`)",
                "ENGINE=MyISAM DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci"};
    }
}
