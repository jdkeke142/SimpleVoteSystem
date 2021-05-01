package fr.keke142.simplevotesystem.websites;

import fr.keke142.simplevotesystem.AbstractHasVoted;
import fr.keke142.simplevotesystem.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

public class Top_ServeursNetHasVoted extends AbstractHasVoted {
    public Top_ServeursNetHasVoted() {
        super();
    }

    @Override
    public int hasVoted(ProxiedPlayer player) {
        JSONObject result = ReadersUtil.readJsonFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (result.getBoolean("success")) {
            return result.getInt("duration") * 60;
        } else {
            return -1;
        }
    }

    @Override
    public String getUserFriendlyName() {
        return "Top-Serveurs.net";
    }

    @Override
    public String getWebsiteName() {
        return "top_ServeursNet";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://api.top-serveurs.net/v1/votes/check-ip?server_token=" + serverId + "&ip=" + playerIp;
    }
}
