package fr.keke142.simplevotesystem.websites;

import fr.keke142.simplevotesystem.AbstractHasVoted;
import fr.keke142.simplevotesystem.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

public class Liste_ServeursFrHasVoted extends AbstractHasVoted {
    @Override
    public int hasVoted(ProxiedPlayer player) {
        JSONObject result = ReadersUtil.readJsonFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (result.getBoolean("success")) {
            int nextVoteTimestamp = result.getInt("prochainVote");

            long currentDateTime = System.currentTimeMillis() / 1000;
            return (int) (nextVoteTimestamp - currentDateTime);
        } else {
            return -1;
        }
    }

    @Override
    public String getUserFriendlyName() {
        return "Liste-Serveurs.fr";
    }

    @Override
    public String getWebsiteName() {
        return "liste_ServeursFr";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://www.liste-serveurs.fr/api/checkVote/" + serverId + "/" + playerIp;
    }
}
