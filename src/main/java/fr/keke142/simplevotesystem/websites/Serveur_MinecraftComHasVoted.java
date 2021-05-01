package fr.keke142.simplevotesystem.websites;

import fr.keke142.simplevotesystem.AbstractHasVoted;
import fr.keke142.simplevotesystem.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

public class Serveur_MinecraftComHasVoted extends AbstractHasVoted {
    public Serveur_MinecraftComHasVoted() {
        super();
    }

    @Override
    public int hasVoted(ProxiedPlayer player) {
        JSONObject result = ReadersUtil.readJsonFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (result.getInt("vote") == 1) {
            return result.getInt("time_until_next_vote");
        } else {
            return -1;
        }
    }

    @Override
    public String getUserFriendlyName() {
        return "Serveur-Minecraft.com";
    }

    @Override
    public String getWebsiteName() {
        return "serveur_MinecraftCom";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://serveur-minecraft.com/api/1/vote/" + serverId + "/" + playerIp + "/json";
    }
}
