package fr.keke142.ivernyavotes;

import fr.keke142.ivernyavotes.managers.VotesManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public abstract class AbstractHasVoted {
    private Configuration config;

    public AbstractHasVoted() {
        File file = new File(IvernyaVotesPlugin.getInstance().getDataFolder(), "config.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            final Iterator<ProxiedPlayer>[] iterator = new Iterator[]{IvernyaVotesPlugin.getInstance().getProxy().getPlayers().iterator()};

            IvernyaVotesPlugin.getInstance().getProxy().getScheduler().schedule(IvernyaVotesPlugin.getInstance(), () -> {
                //It's here to allow dynamic enabling depending of the config.
                if (isEnabledWebsite()) {
                    Collection<ProxiedPlayer> players = IvernyaVotesPlugin.getInstance().getProxy().getPlayers();

                    if (players.isEmpty()) {
                        return;
                    }

                    if (!iterator[0].hasNext()) {
                        iterator[0] = players.iterator();
                    }

                    ProxiedPlayer player = iterator[0].next();

                    if (player != null) {
                        handleVote(player);
                    }
                }
            }, 0, 3, TimeUnit.SECONDS);

        } catch (IOException e) {
            IvernyaVotesPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to load configuration", e);
        }
    }

    public int canVote(ProxiedPlayer player) {
        VotesManager votesManager = IvernyaVotesPlugin.getInstance().getVotesManager();

        int nextVote = votesManager.getNextVote(getWebsiteName(), player);

        if (nextVote < 0) {
            return -1;
        }

        return nextVote;
    }

    public void handleVote(ProxiedPlayer player) {
        VotesManager votesManager = IvernyaVotesPlugin.getInstance().getVotesManager();

        if (votesManager.getNextVote(getWebsiteName(), player) < 0) {
            int hasVoted = hasVoted(player);
            if (hasVoted > 0) {
                votesManager.saveVote(getWebsiteName(), player, hasVoted);

                votesManager.confirmVote(player, getUserFriendlyName());
            }
        }
    }

    public String getServerIdForWebsite() {
        return getConfig().getString("websites." + getWebsiteName() + ".serverId");
    }

    public String getVotePageForWebsite() {
        return getConfig().getString("websites." + getWebsiteName() + ".votePage");
    }

    public boolean isEnabledWebsite() {
        return getConfig().getBoolean("websites." + getWebsiteName() + ".enabled");
    }

    public Configuration getConfig() {
        return config;
    }

    /**
     * @param player The player whose IP we want to check on the voting website
     * @return The time before the next vote in seconds if a vote is detected, or -1 if no vote is detected.
     */
    public abstract int hasVoted(ProxiedPlayer player);

    /**
     * @return The website name as it should be in the configuration and database.
     */
    public abstract String getWebsiteName();

    /**
     * @return The website name as it should be displayed in the voting comand, and the websiteName placeholder
     */
    public abstract String getUserFriendlyName();

    /**
     * @param serverId The ID or the token of the server on the voting website
     * @param playerIp The IP address we want to verify
     * @return
     */
    public abstract String getUrl(String serverId, String playerIp);
}
