package fr.keke142.simplevotesystem.listeners;

import fr.keke142.simplevotesystem.SimpleVoteSystemPlugin;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class NamesListener implements Listener {
    private SimpleVoteSystemPlugin plugin;

    public NamesListener(SimpleVoteSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        plugin.getVotesManager().addOrUpdateName(e.getPlayer());
    }
}
