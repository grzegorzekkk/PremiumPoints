package pl.stillcraft.grzegorzekkk.premiumpoints.messages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class Messenger {

    private Messenger() {
    }

    public static void broadcast(String msg) {
        Bukkit.broadcastMessage(formatMsg(msg));
    }

    public static void broadcast(Locale l) {
        Bukkit.broadcastMessage(formatMsg(MessageStorage.getInstance().getMessage(l)));
    }

    public static void send(final CommandSender sender, String msg) {
        sender.sendMessage(formatMsg(msg));
    }

    public static void send(final CommandSender sender, String[] msgs) {
        Arrays.stream(msgs).forEach(a -> sender.sendMessage(formatMsg(a)));
    }

    public static void send(final CommandSender sender, Locale l) {
        sender.sendMessage(formatMsg(MessageStorage.getInstance().getMessage(l)));
    }

    public static void send(final CommandSender sender, Locale... l) {
        Arrays.stream(l)
                .forEach(
                        a ->
                                sender.sendMessage(
                                        formatMsg(MessageStorage.getInstance().getMessage(a))));
    }

    private static String formatMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
