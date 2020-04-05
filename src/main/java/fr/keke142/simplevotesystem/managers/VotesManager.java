package fr.keke142.simplevotesystem.managers;

import fr.keke142.simplevotesystem.SimpleVoteSystemPlugin;
import fr.keke142.simplevotesystem.objects.Vote;
import fr.keke142.simplevotesystem.utils.MapUtil;
import fr.keke142.simplevotesystem.utils.NumbersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VotesManager {
    private SimpleVoteSystemPlugin plugin;

    public VotesManager(SimpleVoteSystemPlugin plugin) {
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

    public Map<String, Integer> getTopVoters(int amount, int offset) {
        LinkedHashMap<String, Integer> uuidVotes = plugin.getDatabaseManager().getVotesCountsTable().getAllCounts();

        return MapUtil.skipAndTake(uuidVotes, offset, amount);
    }

    public void addOrUpdateName(ProxiedPlayer player) {
        plugin.getDatabaseManager().getVotesNamesTable().addOrUpdateName(player.getUniqueId().toString(), player.getName());
    }

    public String getNameFromUuid(String votePlayer) {
        return plugin.getDatabaseManager().getVotesNamesTable().getNameFromUuid(votePlayer);
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
