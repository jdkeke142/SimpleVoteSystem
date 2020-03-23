package fr.keke142.ivernyavotes.tasks;

import fr.keke142.ivernyavotes.IvernyaVotesPlugin;
import fr.keke142.ivernyavotes.managers.VotesManager;
import fr.keke142.ivernyavotes.objects.Vote;
import fr.keke142.ivernyavotes.utils.DateUtil;

import java.util.Date;

public class VoteCheckerTask implements Runnable {
    private IvernyaVotesPlugin plugin;

    public VoteCheckerTask(IvernyaVotesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        VotesManager votesManager = plugin.getVotesManager();

        for (Vote vote : votesManager.getVotes()) {
            Date dateTimeNextVote = DateUtil.addSecondsToDate(vote.getCheckDate(), vote.getNextVote());

            if (dateTimeNextVote.before(new Date())) {
                String votePlayer = vote.getVotePlayer();

                votesManager.removeVote(vote.getVoteWebsite(), votePlayer);
            }
        }
    }
}
