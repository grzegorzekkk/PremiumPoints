package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.CommandManager;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;

public interface SubCMD {

    String getPermission();

    void onCommand(CommandSender sender, Command cmd, String label, String[] args);

    default boolean needsPlayer() {
        return false;
    }

    default boolean runCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(CommandManager.PERM_BEGIN + getPermission()) && !sender.isOp()) {
            Messenger.send(sender, Locale.NO_PERMISSION);
        } else {
            if (needsPlayer()) {
                if (sender instanceof Player) {
                    onCommand(sender, cmd, label, args);
                } else {
                    Messenger.send(sender, Locale.PLUGIN_HEADER, Locale.PLAYER_NEED);
                }
            } else {
                onCommand(sender, cmd, label, args);
            }
        }

        return true;
    }
}
