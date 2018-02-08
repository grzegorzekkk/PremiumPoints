package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;

public class GivePscCMD implements SubCMD {

    private PaymentDao paymentDao;

    public GivePscCMD(PaymentDao paymentDaoArg) {
        paymentDao = paymentDaoArg;
    }

    @Override
    public boolean needsPlayer() {
        return false;
    }

    @Override
    public String getPermission() {
        return "givepsc";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if ((sender instanceof Player && !sender.getName().equals("ziutekkk"))
                || sender instanceof BlockCommandSender) {
            return;
        }

        if (args.length == 4) {
            String playerName = args[1];
            String secret = ConfigStorage.getInstance().getSecret();

            int ppAmount;
            try {
                ppAmount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                e.getMessage();
                sender.sendMessage("Invalid amount of pp");
                return;
            }
            if (!args[3].equalsIgnoreCase(secret)) {
                Messenger.send(sender, "Invalid secret");
                return;
            }

            Messenger.send(sender, "Successfully send " + args[2] + " pp to player " + playerName);

            new BukkitRunnable() {
                @Override
                public void run() {
                    paymentDao.addPayment(playerName, ppAmount, true);
                }
            }.runTaskAsynchronously(PremiumPoints.getInstance());
        }
    }
}
