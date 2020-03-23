package fr.keke142.ivernyavotes.managers;

import fr.keke142.ivernyavotes.IvernyaVotesPlugin;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MessageManager {
    private static final Pattern PATTERN_DBLQUOTES = Pattern.compile("''");
    private static ResourceBundle messagesDefault;
    private static ResourceBundle messagesSetting;

    public static void loadLocale(IvernyaVotesPlugin plugin, Locale locale) {
        plugin.createDefaultConfiguration(new File(plugin.getDataFolder(), "messages_fr.properties"), "messages_fr.properties");
        try {
            URL[] urls = {plugin.getDataFolder().toURI().toURL()};
            messagesDefault = ResourceBundle.getBundle("messages", Locale.FRENCH);
            messagesSetting = ResourceBundle.getBundle("messages", locale, new URLClassLoader(urls));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (MissingResourceException e) {
            plugin.getLogger().severe("Failed to load locale...");
            e.printStackTrace();
        }
        plugin.getLogger().info("Loaded locale");
    }

    public static String msg(String identifier) {
        try {
            if (messagesSetting != null)
                return ChatColor.translateAlternateColorCodes('&', PATTERN_DBLQUOTES.matcher(messagesSetting.getString(identifier)).replaceAll("'"));
            if (messagesDefault != null)
                return ChatColor.translateAlternateColorCodes('&', PATTERN_DBLQUOTES.matcher(messagesDefault.getString(identifier)).replaceAll("'"));
        } catch (Exception exception) {
        }
        return "{" + identifier + "}";
    }

    public static String msg(String identifier, Object... args) {
        try {
            if (messagesSetting != null) {
                MessageFormat formatter = new MessageFormat(ChatColor.translateAlternateColorCodes('&', messagesSetting.getString(identifier)));
                return formatter.format(args);
            }
            if (messagesDefault != null) {
                MessageFormat formatter = new MessageFormat(ChatColor.translateAlternateColorCodes('&', messagesDefault.getString(identifier)));
                return formatter.format(args);
            }
        } catch (Exception exception) {
        }
        return "{" + identifier + "}:" + args;
    }
}
