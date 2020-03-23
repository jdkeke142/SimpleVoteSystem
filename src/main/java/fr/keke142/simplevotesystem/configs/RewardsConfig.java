package fr.keke142.simplevotesystem.configs;

import net.md_5.bungee.config.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RewardsConfig {
    private Map<Integer, List<String>> rewards;

    public void load(Configuration config) {
        rewards = new TreeMap<>(Collections.reverseOrder());
        for (String reward : config.getSection("rewards").getKeys()) {
            rewards.put(Integer.valueOf(reward), config.getStringList("rewards." + reward));
        }
    }

    public void save(Configuration config) {
        config.set("rewards", rewards);
    }

    public Map<Integer, List<String>> getRewards() {
        return rewards;
    }
}
