package fr.keke142.ivernyavotes.commands;

import fr.keke142.ivernyavotes.AbstractHasVoted;
import fr.keke142.ivernyavotes.IvernyaVotesPlugin;
import fr.keke142.ivernyavotes.managers.MessageManager;
import fr.keke142.ivernyavotes.utils.ChatUtil;
import fr.keke142.ivernyavotes.utils.DateUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Date;

public class VoteCommand extends Command {
    private IvernyaVotesPlugin plugin;

    public VoteCommand(IvernyaVotesPlugin plugin) {
        super("vote");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(TextComponent.fromLegacyText(MessageManager.msg("commands.onlyPlayer")));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) commandSender;
        ChatUtil.sendCenteredMessage(p, MessageManager.msg("commands.vote.header"));

        int counts = plugin.getVotesManager().getCounts(p.getUniqueId().toString());
        ChatUtil.sendCenteredMessage(p, MessageManager.msg("commands.vote.count", counts));

        Date currentDate = new Date();

        for (AbstractHasVoted website : plugin.getWebsites()) {
            if (!website.isEnabledWebsite()) continue;

            int canVote = website.canVote(p);

            String hoverMessage = MessageManager.msg("commands.vote.clickHereToOpen");
            if (canVote == -1) {
                String message = MessageManager.msg("commands.vote.entry.canVote", website.getUserFriendlyName(), website.getVotePageForWebsite(), hoverMessage);

                p.sendMessage(ComponentSerializer.parse(message));
            } else {
                Date checkDate = plugin.getVotesManager().getCheckDate(website.getWebsiteName(), p);

                Date dateTimeCanVote = DateUtil.addSecondsToDate(checkDate, canVote);

                long secondsDifference = (dateTimeCanVote.getTime() - currentDate.getTime()) / 1000;

                long hours = secondsDifference / 3600;
                long minutes = (secondsDifference % 3600) / 60;
                long seconds = secondsDifference % 60;

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                String message = MessageManager.msg("commands.vote.entry.canVoteIn", website.getUserFriendlyName(), website.getVotePageForWebsite(), hoverMessage, timeString);

                p.sendMessage(ComponentSerializer.parse(message));
            }

        }
    }
}
