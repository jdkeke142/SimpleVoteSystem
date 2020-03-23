package fr.keke142.simplevotesystem.managers;

import com.zaxxer.hikari.HikariDataSource;
import fr.keke142.simplevotesystem.SimpleVoteSystemPlugin;
import fr.keke142.simplevotesystem.database.VotesCountsTable;
import fr.keke142.simplevotesystem.database.VotesTable;

public class DatabaseManager {
    private SimpleVoteSystemPlugin plugin;
    private VotesTable votesTable;
    private VotesCountsTable votesCountsTable;

    private HikariDataSource hikari;

    public DatabaseManager(SimpleVoteSystemPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadDatabase() {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", plugin.getConfigManager().getDatabaseConfig().getHost());
        hikari.addDataSourceProperty("port", plugin.getConfigManager().getDatabaseConfig().getPort());
        hikari.addDataSourceProperty("databaseName", plugin.getConfigManager().getDatabaseConfig().getDatabase());
        hikari.addDataSourceProperty("user", plugin.getConfigManager().getDatabaseConfig().getUserName());
        hikari.addDataSourceProperty("password", plugin.getConfigManager().getDatabaseConfig().getPassword());

        votesTable = new VotesTable(this);
        votesTable.createTable();

        votesCountsTable = new VotesCountsTable(this);
        votesCountsTable.createTable();
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public VotesTable getVotesTable() {
        return votesTable;
    }

    public VotesCountsTable getVotesCountsTable() {
        return votesCountsTable;
    }
}
