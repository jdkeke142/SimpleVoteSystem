package fr.keke142.ivernyavotes.tasks;

import fr.keke142.ivernyavotes.IvernyaVotesPlugin;
import fr.keke142.ivernyavotes.configs.CleanerConfig;
import fr.keke142.ivernyavotes.managers.VotesManager;

import java.util.Calendar;

public class VoteCleanerTask implements Runnable {
    private IvernyaVotesPlugin plugin;

    public VoteCleanerTask(IvernyaVotesPlugin plugin) {
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
