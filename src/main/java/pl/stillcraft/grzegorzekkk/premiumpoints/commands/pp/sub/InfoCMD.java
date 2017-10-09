package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.hooks.PlayerPointsHook;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;

public class InfoCMD implements SubCMD {

    @Override
    public boolean needsPlayer() {
        return true;
    }

    @Override
    public String getPermission() {
        return "info";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        String infoMsg =
                MessageStorage.getInstance()
                        .getMessage(Locale.INFO)
                        .replaceAll("%playername%", p.getName())
                        .replaceAll("%player_pp%", String.valueOf(PlayerPointsHook.getPoints(p)));

        String header = MessageStorage.getInstance().getMessage(Locale.PLUGIN_HEADER);

        Messenger.send(sender, new String[]{header, infoMsg});
    }
}
