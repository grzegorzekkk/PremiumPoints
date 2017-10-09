package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.hooks.PlayerPointsHook;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;

public class PayCMD implements SubCMD {

    @Override
    public boolean needsPlayer() {
        return true;
    }

    @Override
    public String getPermission() {
        return "pay";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (args.length != 3) {
            Messenger.send(p, Locale.PLUGIN_HEADER, Locale.PAY);
        } else {
            int points;
            Player receiver;

            try {
                points = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                e.getMessage();
                Messenger.send(p, Locale.PLUGIN_HEADER, Locale.PAY_FAIL);
                return;
            }

            if ((receiver = Bukkit.getPlayerExact(args[1])) == null) {
                Messenger.send(p, Locale.PLUGIN_HEADER, Locale.PAY_FAIL);
            } else if (PlayerPointsHook.hasPoints(p, points)) {
                PlayerPointsHook.takePoints(p, points);
                PlayerPointsHook.givePoints(receiver, points);
                Messenger.send(
                        p,
                        MessageStorage.getInstance()
                                .getMessage(Locale.PAY_SUCC)
                                .replaceAll("%pp%", String.valueOf(points))
                                .replaceAll("%player%", receiver.getName()));
                Messenger.send(
                        receiver,
                        MessageStorage.getInstance()
                                .getMessage(Locale.PAY_RECEIVED)
                                .replaceAll("%pp%", String.valueOf(points))
                                .replaceAll("%player%", p.getName()));
            } else {
                Messenger.send(p, Locale.PLUGIN_HEADER, Locale.PAY_NOT_ENOUGH);
            }
        }
    }
}
