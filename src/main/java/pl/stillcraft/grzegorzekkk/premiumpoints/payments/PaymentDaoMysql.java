package pl.stillcraft.grzegorzekkk.premiumpoints.payments;

import javafx.util.Pair;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PaymentDaoMysql implements PaymentDao {

    private DataSource ds;

    public PaymentDaoMysql(DataSource dataSource) {
        ds = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * {@inheritDoc}
     */
    public void createTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS payment_history "
                            + "(id INT(11) NOT NULL AUTO_INCREMENT, user VARCHAR(16), pp INT(5), type VARCHAR(5),"
                            + " time DATETIME, isCollected BOOLEAN, PRIMARY KEY (id));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> allPayments = new LinkedList<>();

        try (Connection connection = getConnection();
             PreparedStatement select =
                     connection.prepareStatement("SELECT * FROM payment_history;")) {

            ResultSet resultSet = select.executeQuery();

            while (resultSet.next()) {
                Payment newPayment = new Payment();
                newPayment.setId(resultSet.getInt("id"));
                newPayment.setUser(resultSet.getString("user"));
                newPayment.setPp(resultSet.getInt("pp"));
                newPayment.setType(resultSet.getString("type"));
                newPayment.setTime(resultSet.getDate("time"));
                newPayment.setCollected(resultSet.getBoolean("isCollected"));
                allPayments.add(newPayment);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPayments;
    }

    @Override
    public List<Payment> getLastPlayerPayments(Player p) {
        List<Payment> playerPayments = new LinkedList<>();

        try (Connection connection = getConnection();
             PreparedStatement select =
                     connection.prepareStatement(
                             "SELECT * from payment_history WHERE user=? ORDER BY time DESC LIMIT 5;")) {

            select.setString(1, p.getName());
            ResultSet resultSet = select.executeQuery();

            while (resultSet.next()) {
                Payment newPayment = new Payment();
                newPayment.setId(resultSet.getInt("id"));
                newPayment.setUser(resultSet.getString("user"));
                newPayment.setPp(resultSet.getInt("pp"));
                newPayment.setType(resultSet.getString("type"));
                newPayment.setTime(resultSet.getTimestamp("time").getTime());
                newPayment.setCollected(resultSet.getBoolean("isCollected"));
                playerPayments.add(newPayment);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerPayments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pair<String, Integer>> getTopMonthPlayers() {
        List<Pair<String, Integer>> topPlayers = new LinkedList<>();

        try (Connection connection = getConnection();
             PreparedStatement select =
                     connection.prepareStatement(
                             "SELECT user, (SELECT sum(pp)) as sum_pp from payment_history"
                                     + " WHERE month(date(time))=month(date(now())) group BY user"
                                     + " ORDER BY sum_pp DESC LIMIT 5;")) {

            ResultSet resultSet = select.executeQuery();

            while (resultSet.next()) {
                topPlayers.add(new Pair<>(resultSet.getString("user"), resultSet.getInt("sum_pp")));
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topPlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> getNotCollectedPayments() {
        List<Payment> notCollectedPayments = new LinkedList<>();

        try (Connection connection = getConnection();
             PreparedStatement select =
                     connection.prepareStatement(
                             "SELECT * FROM payment_history WHERE isCollected=false;")) {

            ResultSet resultSet = select.executeQuery();

            while (resultSet.next()) {
                Payment newPayment = new Payment();
                newPayment.setId(resultSet.getInt("id"));
                newPayment.setUser(resultSet.getString("user"));
                newPayment.setPp(resultSet.getInt("pp"));
                newPayment.setType(resultSet.getString("type"));
                newPayment.setTime(resultSet.getTimestamp("time").getTime());
                newPayment.setCollected(resultSet.getBoolean("isCollected"));
                notCollectedPayments.add(newPayment);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notCollectedPayments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeCollectedStatus(int paymentId) {
        try (Connection connection = getConnection();
             PreparedStatement update =
                     connection.prepareStatement(
                             "UPDATE payment_history SET isCollected=true WHERE id=?;")) {

            update.setInt(1, paymentId);
            update.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPayment(String playerName, int givePoints, boolean isPaysafecard) {
        try (Connection connection = getConnection();
             PreparedStatement insert =
                     connection.prepareStatement(
                             "INSERT INTO payment_history (user, pp, type, time, isCollected)"
                                     + " VALUES (?, ?, ?, now(), ?);")) {

            insert.setString(1, playerName);
            insert.setInt(2, givePoints);
            if (isPaysafecard) {
                insert.setString(3, "PSC");
                insert.setBoolean(4, false);
            } else {
                insert.setString(3, "SMS");
                insert.setBoolean(4, true);
            }

            insert.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
