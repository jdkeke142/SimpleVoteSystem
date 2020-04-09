package fr.keke142.simplevotesystem.websites;

import fr.keke142.simplevotesystem.AbstractHasVoted;
import fr.keke142.simplevotesystem.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

public class Liste_ServeurFrHasVoted extends AbstractHasVoted {
    public Liste_ServeurFrHasVoted() {
        super();
    }

    @Override
    public int hasVoted(ProxiedPlayer player) {
        JSONObject result = ReadersUtil.readJsonFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (result.getBoolean("hasVoted")) {
            return result.getInt("nextVote");
        } else {
            return -1;
        }
    }

    @Override
    public String getUserFriendlyName() {
        return "Liste-Serveur.fr";
    }

    @Override
    public String getWebsiteName() {
        return "liste_ServeurFr";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://www.liste-serveur.fr/api/hasVoted/" + serverId + "/" + playerIp;
    }
}
