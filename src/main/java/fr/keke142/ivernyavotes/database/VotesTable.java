package fr.keke142.ivernyavotes.database;

import fr.keke142.ivernyavotes.managers.DatabaseManager;
import fr.keke142.ivernyavotes.objects.Vote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VotesTable extends AbstractTable {
    private static final String SAVE_VOTE = "INSERT INTO ivv_votes (voteWebsite, votePlayer, nextVote) VALUES(?,?,?)";
    private static final String REMOVE_VOTE = "DELETE FROM ivv_votes WHERE voteWebsite=? AND votePlayer=?";
    private static final String GET_VOTES = "SELECT * FROM ivv_votes";
    private static final String GET_NEXT_VOTE = "SELECT nextVote FROM ivv_votes WHERE voteWebsite=? AND votePlayer=?";
    private static final String GET_CHECK_DATE = "SELECT checkDate FROM ivv_votes WHERE voteWebsite=? AND votePlayer=?";

    public VotesTable(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    public void saveVote(String voteWebsite, String votePlayer, int nextVote) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement saveVote = connection.prepareStatement(SAVE_VOTE)) {

            saveVote.setString(1, voteWebsite);
            saveVote.setString(2, votePlayer);
            saveVote.setInt(3, nextVote);

            saveVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVote(String voteWebsite, String votePlayer) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement removeVote = connection.prepareStatement(REMOVE_VOTE)) {

            removeVote.setString(1, voteWebsite);
            removeVote.setString(2, votePlayer);

            removeVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vote> getVotes() {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement getVotes = connection.prepareStatement(GET_VOTES)) {

            List<Vote> votes = new ArrayList<>();
            ResultSet res = getVotes.executeQuery();
            while (res.next()) {
                String websiteResult = res.getString(1);
                String votePlayerResult = res.getString(2);
                Date checkDateResult = res.getTimestamp(3);
                int nextVoteReslt = res.getInt(4);

                votes.add(new Vote(websiteResult, votePlayerResult, checkDateResult, nextVoteReslt));
            }

            return votes;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getNextVote(String voteWebsite, String votePlayer) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement getNextVote = connection.prepareStatement(GET_NEXT_VOTE)) {

            getNextVote.setString(1, voteWebsite);
            getNextVote.setString(2, votePlayer);

            ResultSet res = getNextVote.executeQuery();
            if (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Date getCheckDate(String voteWebsite, String votePlayer) {
        try (Connection connection = getDatabaseManager().getHikari().getConnection();
             PreparedStatement getCheckDate = connection.prepareStatement(GET_CHECK_DATE)) {

            getCheckDate.setString(1, voteWebsite);
            getCheckDate.setString(2, votePlayer);

            ResultSet res = getCheckDate.executeQuery();
            if (res.next()) {
                return res.getTimestamp(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String[] getTable() {
        return new String[]{"ivv_votes", "`voteWebsite` VARCHAR(36) NOT NULL, " +
                "`votePlayer` VARCHAR(36) NOT NULL, " +
                "`checkDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "`nextVote` INT NOT NULL," +
                "PRIMARY KEY (`voteWebsite`, `votePlayer`)",
                "ENGINE=MyISAM DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci"};
    }
}
