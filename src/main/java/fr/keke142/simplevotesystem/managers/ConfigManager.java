package fr.keke142.simplevotesystem.managers;

import fr.keke142.simplevotesystem.SimpleVoteSystemPlugin;
import fr.keke142.simplevotesystem.configs.CleanerConfig;
import fr.keke142.simplevotesystem.configs.DatabaseConfig;
import fr.keke142.simplevotesystem.configs.RewardsConfig;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private SimpleVoteSystemPlugin plugin;

    private DatabaseConfig databaseConfig = new DatabaseConfig();
    private RewardsConfig rewardsConfig = new RewardsConfig();
    private CleanerConfig cleanerConfig = new CleanerConfig();

    private File file;
    private Configuration config;

    public ConfigManager(SimpleVoteSystemPlugin plugin) throws IOException {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "config.yml");
        plugin.createDefaultConfiguration(file, "config.yml");

        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

        databaseConfig.load(config);
        rewardsConfig.load(config);
        cleanerConfig.load(config);
    }

    public void save() {
        try {
            databaseConfig.save(config);
            rewardsConfig.save(config);
            cleanerConfig.save(config);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() throws IOException {
        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

        databaseConfig.load(config);
        rewardsConfig.load(config);
        cleanerConfig.load(config);
    }

    public CleanerConfig getCleanerConfig() {
        return cleanerConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public RewardsConfig getRewardsConfig() {
        return rewardsConfig;
    }

}
