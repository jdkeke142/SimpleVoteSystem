package fr.keke142.simplevotesystem.tasks;

import fr.keke142.simplevotesystem.SimpleVoteSystemPlugin;
import fr.keke142.simplevotesystem.managers.VotesManager;
import fr.keke142.simplevotesystem.objects.Vote;
import fr.keke142.simplevotesystem.utils.DateUtil;

import java.util.Date;

public class VoteCheckerTask implements Runnable {
    private SimpleVoteSystemPlugin plugin;

    public VoteCheckerTask(SimpleVoteSystemPlugin plugin) {
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
