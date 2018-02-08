package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.hooks.PlayerPointsHook;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.Payment;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.psc.CollectPpTask;

import java.util.List;
import java.util.stream.Collectors;

public class CollectCMD implements SubCMD {

    private CollectPpTask collectPpTask;
    private PaymentDao paymentDao;

    public CollectCMD(CollectPpTask collectPpTaskArg, PaymentDao paymentDaoArg) {
        collectPpTask = collectPpTaskArg;
        paymentDao = paymentDaoArg;
    }

    @Override
    public String getPermission() {
        return "collect";
    }

    @Override
    public boolean needsPlayer() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        collectPpTask.refreshNotCollectedPayments();
        List<Payment> allPayments = collectPpTask.getNotCollectedPayments();
        List<Payment> senderPayments =
                allPayments
                        .stream()
                        .filter(payment -> payment.getUser().equals(p.getName()))
                        .collect(Collectors.toList());

        if (senderPayments.isEmpty()) {
            Messenger.send(p, Locale.PLUGIN_HEADER, Locale.COLLECT_FAIL);
            return;
        }

        for (Payment payment : senderPayments) {
            PlayerPointsHook.givePoints(p, payment.getPp());
            Messenger.send(p, Locale.PLUGIN_HEADER, Locale.COLLECT_SUCC);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Payment payment : senderPayments) {
                    paymentDao.changeCollectedStatus(payment.getId());
                }
            }
        }.runTaskAsynchronously(PremiumPoints.getInstance());
    }
}
