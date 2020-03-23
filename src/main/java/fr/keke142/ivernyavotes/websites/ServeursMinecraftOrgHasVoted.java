package fr.keke142.ivernyavotes.websites;

import fr.keke142.ivernyavotes.AbstractHasVoted;
import fr.keke142.ivernyavotes.utils.NumbersUtil;
import fr.keke142.ivernyavotes.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServeursMinecraftOrgHasVoted extends AbstractHasVoted {
    public ServeursMinecraftOrgHasVoted() {
        super();
    }

    @Override
    public int hasVoted(ProxiedPlayer player) {
        String result = ReadersUtil.readFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (NumbersUtil.isNumeric(result)) {
            return Integer.parseInt(result);
        }

        return -1;
    }

    @Override
    public String getWebsiteName() {
        return "serveursMinecraftOrg";
    }

    @Override
    public String getUserFriendlyName() {
        return "ServeursMinecraft.org";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://www.serveursminecraft.org/sm_api/peutVoter.php?id=" + serverId + "&ip=" + playerIp;
    }
}
