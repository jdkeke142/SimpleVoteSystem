package fr.keke142.simplevotesystem.utils;

import fr.keke142.simplevotesystem.DefaultFontInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Chat;

public final class ChatUtil {
    private final static int CENTER_PX = 154;

    private ChatUtil() {
        throw new UnsupportedOperationException();
    }

    public static void sendCenteredMessage(ProxiedPlayer player, String message) {
        sendCenteredMessage(player, message, false);
    }

    public static void sendCenteredMessage(ProxiedPlayer player, String message, boolean isJson) {
        if (message == null || message.equals("")) player.sendMessage(TextComponent.fromLegacyText(""));
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        if (isJson) {
            player.unsafe().sendPacket(new Chat(message));
        } else {
            player.sendMessage(TextComponent.fromLegacyText(sb.toString() + message));
        }
    }
}
