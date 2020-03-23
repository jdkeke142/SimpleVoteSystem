package fr.keke142.ivernyavotes.websites;

import fr.keke142.ivernyavotes.AbstractHasVoted;
import fr.keke142.ivernyavotes.IvernyaVotesPlugin;
import fr.keke142.ivernyavotes.utils.ReadersUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Serveurs_MinecraftOrgHasVoted extends AbstractHasVoted {
    private static final int VOTE_INTERVAL_IN_SECONDS = 86400;

    public Serveurs_MinecraftOrgHasVoted() {
        super();
    }

    @Override
    public int hasVoted(ProxiedPlayer player) {
        JSONObject result = ReadersUtil.readJsonFromUrl(getUrl(getServerIdForWebsite(), player.getAddress().getHostString()));
        if (result.getInt("votes") >= 1) {
            String lastVoteDateString = result.getString("lastVoteDate");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Date lastVoteDate = dateFormatter.parse(lastVoteDateString);

                Date currentDate = new Date();

                long diff = currentDate.getTime() - lastVoteDate.getTime();

                long diffSeconds = diff / 1000 % 60;

                return (int) (VOTE_INTERVAL_IN_SECONDS - diffSeconds);
            } catch (ParseException e) {
                IvernyaVotesPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to parse last vote date for " + getWebsiteName());
                e.printStackTrace();
            }
        }

        return -1;
    }

    @Override
    public String getWebsiteName() {
        return "serveurs_MinecraftOrg";
    }

    @Override
    public String getUserFriendlyName() {
        return "Serveurs-Minecraft.org";
    }

    @Override
    public String getUrl(String serverId, String playerIp) {
        return "https://www.serveurs-minecraft.org/api/is_valid_vote.php?id=" + serverId + "&ip=" + playerIp + "&duration=10&format=json";
    }
}
