package fr.keke142.ivernyavotes.objects;

import java.util.Date;

public class Vote {
    private String voteWebsite;
    private String votePlayer;
    private Date checkDate;
    private int nextVote;

    public Vote(String voteWebsite, String votePlayer, Date checkDate, int nextVote) {
        this.voteWebsite = voteWebsite;
        this.votePlayer = votePlayer;
        this.checkDate = checkDate;
        this.nextVote = nextVote;
    }

    public String getVoteWebsite() {
        return voteWebsite;
    }

    public String getVotePlayer() {
        return votePlayer;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public int getNextVote() {
        return nextVote;
    }
}
