package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.hooks.PlayerPointsHook;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;

public class AddCMD implements SubCMD {
    @Override
    public String getPermission() {
        return "add";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 4) {
            Messenger.send(sender, Locale.INVALID_ARG_AMOUNT);
            return;
        }

        int points;
        String secret = ConfigStorage.getInstance().getSecret();
        Player receiver = Bukkit.getPlayerExact(args[1]);

        try {
            points = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            e.getMessage();
            return;
        }

        if (receiver != null && args[3].equals(secret) && points > 0) {
            PlayerPointsHook.givePoints(receiver, points);
            Messenger.send(
                    receiver,
                    MessageStorage.getInstance()
                            .getMessage(Locale.ADD_RECEIVED_SUCC)
                            .replaceAll("%pp%", String.valueOf(points)));
            if (sender instanceof Player) {
                Messenger.send(
                        sender,
                        MessageStorage.getInstance()
                                .getMessage(Locale.ADD_SEND_SUCC)
                                .replaceAll("%pp%", String.valueOf(points))
                                .replaceAll("%player%", receiver.getName()));
            }
        } else {
            Messenger.send(sender, Locale.INVALID_ARG_ORDER);
        }
    }
}
