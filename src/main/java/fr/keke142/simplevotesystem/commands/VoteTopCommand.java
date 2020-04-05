package fr.keke142.simplevotesystem.commands;

import fr.keke142.simplevotesystem.SimpleVoteSystemPlugin;
import fr.keke142.simplevotesystem.managers.MessageManager;
import fr.keke142.simplevotesystem.utils.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteTopCommand extends Command {
    private SimpleVoteSystemPlugin plugin;

    public VoteTopCommand(SimpleVoteSystemPlugin plugin) {
        super("votetop");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(TextComponent.fromLegacyText(MessageManager.msg("commands.onlyPlayer")));
            return;
        }

        int nPerPage = 10;
        int page = 1;

        ProxiedPlayer p = (ProxiedPlayer) commandSender;

        if (args.length == 1) {
            try {
                page = Math.abs(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                p.sendMessage(TextComponent.fromLegacyText(MessageManager.msg("commands.votetop.invalidNumber")));
                return;
            }
        }

        int offset = (page - 1) * nPerPage;

        int finalPage = page;
        plugin.getProxy().getScheduler()
                .runAsync(plugin,
                        () -> {
                            Map<String, Integer> topVoters = plugin.getVotesManager().getTopVoters(nPerPage, offset);

                            if (topVoters.isEmpty()) {
                                p.sendMessage(TextComponent.fromLegacyText(MessageManager.msg("commands.votetop.notEnoughOnThisPage")));
                                return;
                            }

                            AtomicInteger index = new AtomicInteger(offset + 1);

                            ChatUtil.sendCenteredMessage(p, MessageManager.msg("commands.votetop.header", finalPage));

                            topVoters.forEach((playerUuid, balance) -> {
                                String playerName = plugin.getVotesManager().getNameFromUuid(playerUuid);

                                if (playerName == null) {
                                    playerName = "<unknown>";
                                }

                                p.sendMessage(TextComponent.fromLegacyText(MessageManager.msg("commands.votetop.entry", index.getAndIncrement(), playerName, balance)));
                            });

                            ChatUtil.sendCenteredMessage(p, MessageManager.msg("commands.votetop.footer", finalPage + 1));
                        }
                );
    }
}
