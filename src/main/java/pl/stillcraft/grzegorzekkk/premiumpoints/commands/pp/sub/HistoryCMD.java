package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.Payment;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDaoMysql;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.HikariPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class HistoryCMD implements SubCMD {

    @Override
    public String getPermission() {
        return "history";
    }

    @Override
    public boolean needsPlayer() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Player p = (Player) sender;
        Messenger.send(p, Locale.PLUGIN_HEADER, Locale.HISTORY);

        new BukkitRunnable() {
            @Override
            public void run() {
                PaymentDao pDao = new PaymentDaoMysql(HikariPool.getInstance().getDataSource());
                List<Payment> playerLastPayments =
                        Collections.synchronizedList(pDao.getLastPlayerPayments(p));
                if (playerLastPayments.isEmpty()) {
                    Messenger.send(p, Locale.HISTORY_EMPTY);
                }
                playerLastPayments.forEach(
                        a ->
                                Messenger.send(
                                        p,
                                        MessageStorage.getInstance()
                                                .getMessage(Locale.HISTORY_EACH)
                                                .replaceAll("%pp%", String.valueOf(a.getPp()))
                                                .replaceAll("%type%", a.getType())
                                                .replaceAll(
                                                        "%date%", dateFormat.format(a.getTime()))));
            }
        }.runTaskAsynchronously(PremiumPoints.getInstance());
    }
}
