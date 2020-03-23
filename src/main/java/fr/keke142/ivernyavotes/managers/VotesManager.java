package fr.keke142.ivernyavotes.managers;

import fr.keke142.ivernyavotes.IvernyaVotesPlugin;
import fr.keke142.ivernyavotes.objects.Vote;
import fr.keke142.ivernyavotes.utils.NumbersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class VotesManager {
    private IvernyaVotesPlugin plugin;

    public VotesManager(IvernyaVotesPlugin plugin) {
        this.plugin = plugin;
    }

    public void saveVote(String voteWebsite, ProxiedPlayer player, int nextVote) {
        plugin.getDatabaseManager().getVotesTable().saveVote(voteWebsite, player.getUniqueId().toString(), nextVote);
    }

    public void removeVote(String voteWebsite, String playerUuid) {
        plugin.getDatabaseManager().getVotesTable().removeVote(voteWebsite, playerUuid);
    }

    public List<Vote> getVotes() {
        return plugin.getDatabaseManager().getVotesTable().getVotes();
    }

    public Date getCheckDate(String voteWebsite, ProxiedPlayer player) {
        return plugin.getDatabaseManager().getVotesTable().getCheckDate(voteWebsite, player.getUniqueId().toString());
    }

    public int getNextVote(String voteWebsite, ProxiedPlayer player) {
        return plugin.getDatabaseManager().getVotesTable().getNextVote(voteWebsite, player.getUniqueId().toString());
    }

    private void incrementCounts(String playerUuid) {
        plugin.getDatabaseManager().getVotesCountsTable().incrementCounts(playerUuid);
    }

    public int getCounts(String playerUuid) {
        return plugin.getDatabaseManager().getVotesCountsTable().getCounts(playerUuid);
    }

    public void resetAllCounts() {
        plugin.getDatabaseManager().getVotesCountsTable().resetAllCounts();
    }

    public void confirmVote(ProxiedPlayer votePlayer, String websiteName) {
        String playerUuid = votePlayer.getUniqueId().toString();

        int lastCount = getCounts(playerUuid);
        incrementCounts(playerUuid);

        int newCount = lastCount + 1;

        for (Map.Entry<Integer, List<String>> entry : plugin.getConfigManager().getRewardsConfig().getRewards().entrySet()) {
            Integer step = entry.getKey();
            List<String> cmds = entry.getValue();

            int result = NumbersUtil.roundUp(newCount, step);

            if (newCount == result) {
                for (String cmd : cmds) {
                    plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), cmd.replaceAll("%websiteName%", websiteName)
                            .replaceAll("%playerName%", votePlayer.getName()));
                }
                break;
            }
        }
    }
}
