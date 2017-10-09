package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import javafx.util.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDaoMysql;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.HikariPool;

import java.util.Collections;
import java.util.List;

public class TopCMD implements SubCMD {

    @Override
    public String getPermission() {
        return "top";
    }

    @Override
    public boolean needsPlayer() {
        return false;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Messenger.send(sender, Locale.PLUGIN_HEADER, Locale.TOP);

        new BukkitRunnable() {
            @Override
            public void run() {
                int index = 0;
                PaymentDao pDao = new PaymentDaoMysql(HikariPool.getInstance().getDataSource());
                List<Pair<String, Integer>> topPlayers =
                        Collections.synchronizedList(pDao.getTopMonthPlayers());
                if (topPlayers.isEmpty()) {
                    Messenger.send(sender, Locale.TOP_EMPTY);
                }
                for (Pair<String, Integer> top : topPlayers) {
                    Messenger.send(
                            sender,
                            MessageStorage.getInstance()
                                    .getMessage(Locale.TOP_EACH)
                                    .replaceAll("%id%", String.valueOf(++index))
                                    .replaceAll("%player%", top.getKey())
                                    .replaceAll("%pp%", String.valueOf(top.getValue())));
                }
            }
        }.runTaskAsynchronously(PremiumPoints.getInstance());
    }
}
