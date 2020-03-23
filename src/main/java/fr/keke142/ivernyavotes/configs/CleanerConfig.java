package fr.keke142.ivernyavotes.configs;

import net.md_5.bungee.config.Configuration;

public class CleanerConfig {
    private boolean useCleaner;
    private boolean votesCleaned;

    public void load(Configuration config) {
        useCleaner = config.getBoolean("useCleaner", true);
        votesCleaned = config.getBoolean("votesCleaned", true);
    }

    public void save(Configuration config) {
        config.set("useCleaner", useCleaner);
        config.set("votesCleaned", votesCleaned);
    }

    public void setVotesCleaned(boolean votesCleaned) {
        this.votesCleaned = votesCleaned;
    }


    public boolean useCleaner() {
        return useCleaner;
    }

    public boolean votesCleaned() {
        return votesCleaned;
    }
}
