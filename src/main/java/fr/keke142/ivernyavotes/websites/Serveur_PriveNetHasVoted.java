package fr.keke142.ivernyavotes.websites;

import fr.keke142.ivernyavotes.AbstractHasVoted;
import fr.keke142.ivernyavotes.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

public class Serveur_PriveNetHasVoted extends AbstractHasVoted {
    public Serveur_PriveNetHasVoted() {
        super();
    }

    @Override
    public int hasVoted(ProxiedPlayer player) {
        JSONObject result = ReadersUtil.readJsonFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (result.getInt("status") == 1) {
            return result.getInt("nextvote");
        } else {
            return -1;
        }
    }

    @Override
    public String getUserFriendlyName() {
        return "Serveur-Prive.net";
    }

    @Override
    public String getWebsiteName() {
        return "serveur_PriveNet";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://serveur-prive.net/api/vote/json/" + serverId + "/" + playerIp;
    }
}
