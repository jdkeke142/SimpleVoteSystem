package fr.keke142.simplevotesystem.tasks;

import fr.keke142.simplevotesystem.SimpleVoteSystemPlugin;
import fr.keke142.simplevotesystem.configs.CleanerConfig;
import fr.keke142.simplevotesystem.managers.VotesManager;

import java.util.Calendar;

public class VoteCleanerTask implements Runnable {
    private SimpleVoteSystemPlugin plugin;

    public VoteCleanerTask(SimpleVoteSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        VotesManager votesManager = plugin.getVotesManager();

        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
        int dayOfMonth = calender.get(Calendar.DAY_OF_MONTH);

        CleanerConfig cleanerConfig = plugin.getConfigManager().getCleanerConfig();

        if (dayOfMonth == 1) {
            if (cleanerConfig.votesCleaned()) return;

            votesManager.resetAllCounts();
            cleanerConfig.setVotesCleaned(true);
        } else {
            if (!cleanerConfig.votesCleaned()) return;

            cleanerConfig.setVotesCleaned(false);
        }
        plugin.getConfigManager().save();
    }
}
