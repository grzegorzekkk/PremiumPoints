package pl.stillcraft.grzegorzekkk.premiumpoints.payments;

import javafx.util.Pair;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface PaymentDao {

    Connection getConnection() throws SQLException;

    /**
     * Create SQL table to store pp payment history.
     */
    void createTable();

    List<Payment> getAllPayments();

    List<Payment> getLastPlayerPayments(Player p);

    /**
     * Get top 5 customer of premium points in current month.
     *
     * @return List containing Player and pp amount, they bought this month
     */
    List<Pair<String, Integer>> getTopMonthPlayers();

    /**
     * Get all Paysafecard s, that players didnt collected pp yet.
     *
     * @return List of not ended payments.
     */
    List<Payment> getNotCollectedPayments();

    /**
     * Set payment status as completed.
     *
     * @param paymentId
     */
    void changeCollectedStatus(int paymentId);

    /**
     * Register in database new PremiumPoints payment.
     *
     * @param playerName
     * @param givePoints
     * @param isPaysafecard
     */
    void addPayment(String playerName, int givePoints, boolean isPaysafecard);
}
