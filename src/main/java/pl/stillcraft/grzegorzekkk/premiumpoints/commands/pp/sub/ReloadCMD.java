package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;

/**
 * Reload configuration file command class.
 */
public class ReloadCMD implements SubCMD {

    @Override
    public String getPermission() {
        return "reload";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ConfigStorage.getInstance().reloadConfig();
        MessageStorage.getInstance().reloadConfig();

        Messenger.send(sender, Locale.PLUGIN_HEADER, Locale.CONFIG_RELOADED);
    }
}
