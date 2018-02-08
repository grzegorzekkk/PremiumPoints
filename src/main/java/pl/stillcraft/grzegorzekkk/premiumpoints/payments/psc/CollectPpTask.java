package pl.stillcraft.grzegorzekkk.premiumpoints.payments.psc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.Payment;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;

import java.util.*;

public class CollectPpTask {

    private List<Payment> notCollectedPayments;
    private PaymentDao paymentDao;

    public CollectPpTask(PaymentDao paymentDaoArg) {
        paymentDao = paymentDaoArg;
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
                notCollectedPayments = paymentDao.getNotCollectedPayments();
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
        notCollectedPayments = paymentDao.getNotCollectedPayments();
    }

    public List<Payment> getNotCollectedPayments() {
        if(notCollectedPayments==null){
            return new ArrayList<>();
        }
        return notCollectedPayments;
    }
}
