package pl.stillcraft.grzegorzekkk.premiumpoints.hooks;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerPointsHook {

    private static final String PLUGIN_NOT_FOUND = "PlayerPoints plugin was not found!";
    private static final String INVALID_AMOUNT = "Invalid amount of points: ";
    private static PlayerPoints playerPoints;

    private PlayerPointsHook() {
        throw new IllegalStateException(("Utility class"));
    }

    public static boolean hookPlayerPoints() {
        Plugin pointsPlugin = Bukkit.getPluginManager().getPlugin("PlayerPoints");

        if (pointsPlugin == null) return false;

        playerPoints = (PlayerPoints) pointsPlugin;
        return true;
    }

    public static boolean hasValidPlugin() {
        return playerPoints != null;
    }

    public static int getPoints(Player player) {
        if (!hasValidPlugin()) throw new IllegalStateException(PLUGIN_NOT_FOUND);
        return playerPoints.getAPI().look(player.getUniqueId());
    }

    public static boolean hasPoints(Player player, int minimum) {
        if (!hasValidPlugin()) throw new IllegalStateException(PLUGIN_NOT_FOUND);
        if (minimum < 0) throw new IllegalArgumentException(INVALID_AMOUNT + minimum);

        return playerPoints.getAPI().look(player.getUniqueId()) >= minimum;
    }

    /**
     * @return true if the operation was successful.
     */
    public static boolean takePoints(Player player, int points) {
        if (!hasValidPlugin()) throw new IllegalStateException(PLUGIN_NOT_FOUND);
        if (points < 0) throw new IllegalArgumentException(INVALID_AMOUNT + points);

        return playerPoints.getAPI().take(player.getUniqueId(), points);
    }

    public static boolean givePoints(Player player, int points) {
        if (!hasValidPlugin()) throw new IllegalStateException(PLUGIN_NOT_FOUND);
        if (points < 0) throw new IllegalArgumentException(INVALID_AMOUNT + points);

        return playerPoints.getAPI().give(player.getUniqueId(), points);
    }
}
