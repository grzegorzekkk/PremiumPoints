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
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDaoMysql;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.psc.CollectPpTask;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.HikariPool;

import java.util.List;
import java.util.stream.Collectors;

public class CollectCMD implements SubCMD {

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

        CollectPpTask.getInstance().refreshNotCollectedPayments();
        List<Payment> allPayments = CollectPpTask.getInstance().getNotCollectedPayments();
        List<Payment> senderPayments =
                allPayments
                        .stream()
                        .filter(payment -> payment.getUser().equals(p.getName()))
                        .collect(Collectors.toList());

        if (senderPayments == null || senderPayments.isEmpty()) {
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
                    PaymentDao pDao = new PaymentDaoMysql(HikariPool.getInstance().getDataSource());
                    pDao.changeCollectedStatus(payment.getId());
                }
            }
        }.runTaskAsynchronously(PremiumPoints.getInstance());
    }
}
