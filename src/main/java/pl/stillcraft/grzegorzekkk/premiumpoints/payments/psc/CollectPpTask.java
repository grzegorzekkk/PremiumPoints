package pl.stillcraft.grzegorzekkk.premiumpoints.payments.psc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.Payment;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDaoMysql;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.HikariPool;

import java.util.*;

public class CollectPpTask {

    private static CollectPpTask instance;
    private List<Payment> notCollectedPayments;

    public static CollectPpTask getInstance() {
        if (instance == null) {
            instance = new CollectPpTask();
        }
        return instance;
    }

    /**
     * Starts task which gets all not completed psc payments from database to plugin memory every 3
     * minutes.
     */
    public void startCollecting() {
        notCollectedPayments = Collections.synchronizedList(new ArrayList<>());
        new BukkitRunnable() {
            @Override
            public void run() {
                PaymentDao pDao = new PaymentDaoMysql(HikariPool.getInstance().getDataSource());
                notCollectedPayments = pDao.getNotCollectedPayments();
            }
        }.runTaskTimerAsynchronously(PremiumPoints.getInstance(), 0L, 20L * 60 * 3);
    }

    /**
     * Starts task which informs players, who bought pp by Paysafecard that they didn't redeemed
     * their premium points yet.
     */
    public void startPlayerInforming() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Set<Player> playersToInform = new HashSet<>();
                for (Payment payment : notCollectedPayments) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getName().equals(payment.getUser())) {
                            playersToInform.add(player);
                        }
                    }
                }
                playersToInform.forEach(p -> Messenger.send(p, Locale.COLLECT));
            }
        }.runTaskTimer(PremiumPoints.getInstance(), 20L, 20L * 60);
    }

    /**
     * Instant update of not completed payments to plugin memory.
     */
    public void refreshNotCollectedPayments() {
        PaymentDao pDao = new PaymentDaoMysql(HikariPool.getInstance().getDataSource());
        notCollectedPayments = pDao.getNotCollectedPayments();
    }

    public List<Payment> getNotCollectedPayments() {
        return notCollectedPayments;
    }
}
