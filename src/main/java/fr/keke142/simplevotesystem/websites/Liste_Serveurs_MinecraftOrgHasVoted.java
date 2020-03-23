package fr.keke142.simplevotesystem.websites;

import fr.keke142.simplevotesystem.AbstractHasVoted;
import fr.keke142.simplevotesystem.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Liste_Serveurs_MinecraftOrgHasVoted extends AbstractHasVoted {
    private static final int VOTE_INTERVAL_IN_SECONDS = 10800;
    private static final int CHECK_DURATION_IN_SECONDS = 600;

    public Liste_Serveurs_MinecraftOrgHasVoted() {
        super();
    }

    @Override
    public int hasVoted(ProxiedPlayer player) {
        String result = ReadersUtil.readFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (Integer.parseInt(result) == 1) {
            //Error margin, since we don't know exactly when the player voted during the duration of 10 minutes.
            return VOTE_INTERVAL_IN_SECONDS + CHECK_DURATION_IN_SECONDS;
        }

        return -1;
    }

    @Override
    public String getUserFriendlyName() {
        return "Liste-Serveurs-Minecraft.org";
    }

    @Override
    public String getWebsiteName() {
        return "liste_Serveurs_MinecraftOrg";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://api.liste-serveurs-minecraft.org/vote/vote_verification.php?server_id=" + serverId + "&ip=" + playerIp + "&duration=" + CHECK_DURATION_IN_SECONDS / 60;
    }
}
