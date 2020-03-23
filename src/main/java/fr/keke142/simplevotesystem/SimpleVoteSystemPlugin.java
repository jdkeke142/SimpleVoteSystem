package fr.keke142.simplevotesystem;

import fr.keke142.simplevotesystem.commands.VoteCommand;
import fr.keke142.simplevotesystem.managers.ConfigManager;
import fr.keke142.simplevotesystem.managers.DatabaseManager;
import fr.keke142.simplevotesystem.managers.MessageManager;
import fr.keke142.simplevotesystem.managers.VotesManager;
import fr.keke142.simplevotesystem.tasks.VoteCheckerTask;
import fr.keke142.simplevotesystem.tasks.VoteCleanerTask;
import fr.keke142.simplevotesystem.websites.Liste_Serveurs_MinecraftOrgHasVoted;
import fr.keke142.simplevotesystem.websites.Serveur_PriveNetHasVoted;
import fr.keke142.simplevotesystem.websites.ServeursMinecraftOrgHasVoted;
import fr.keke142.simplevotesystem.websites.Serveurs_MinecraftOrgHasVoted;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

public class SimpleVoteSystemPlugin extends Plugin implements Listener {
    private static SimpleVoteSystemPlugin instance;

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private VotesManager votesManager;

    private List<AbstractHasVoted> websites;

    public static SimpleVoteSystemPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            configManager = new ConfigManager(this);
            databaseManager = new DatabaseManager(this);

            databaseManager.loadDatabase();

            votesManager = new VotesManager(this);

            MessageManager.loadLocale(this, Locale.FRENCH);

            websites = new ArrayList<>();

            websites.add(new Serveurs_MinecraftOrgHasVoted());
            websites.add(new ServeursMinecraftOrgHasVoted());
            websites.add(new Serveur_PriveNetHasVoted());
            websites.add(new Liste_Serveurs_MinecraftOrgHasVoted());

            getProxy().getPluginManager().registerCommand(this, new VoteCommand(this));
            this.getProxy().getPluginManager().registerListener(this, this);

            this.getProxy().getScheduler().schedule(this, new VoteCheckerTask(this), 1L, 1L, TimeUnit.SECONDS);

            if (configManager.getCleanerConfig().useCleaner()) {
                this.getProxy().getScheduler().schedule(this, new VoteCleanerTask(this), 1L, 1L, TimeUnit.HOURS);
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to load the configuration file!", e);
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Create a default configuration file from the .jar.
     *
     * @param actual      The destination file
     * @param defaultName The name of the file inside the jar's defaults folder
     */
    public void createDefaultConfiguration(File actual, String defaultName) {

        // Make parent directories
        File parent = actual.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (actual.exists()) {
            return;
        }

        JarFile file = null;
        InputStream input = null;
        try {
            file = new JarFile(getFile());
            ZipEntry copy = file.getEntry(defaultName);
            if (copy == null) {
                file.close();
                throw new FileNotFoundException();
            }
            input = file.getInputStream(copy);
        } catch (IOException e) {
            getLogger().severe("Unable to read default configuration: " + defaultName);
        }

        if (input != null) {
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(actual);
                byte[] buf = new byte[8192];
                int length = 0;
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }

                getLogger().info("Default configuration file written: " + actual.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException ignore) {
                }

                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ignore) {
                }
            }
        }
        if (file != null) {
            try {
                file.close();
            } catch (IOException ignore) {
            }
        }
    }

    public VotesManager getVotesManager() {
        return votesManager;
    }

    public List<AbstractHasVoted> getWebsites() {
        return websites;
    }
}
