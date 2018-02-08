package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTabCompleter implements TabCompleter {

    private static final List<String> COMMANDS_TO_COMPLETE =
            Arrays.asList("pay", "daj", "givepsc", "add");

    @Override
    public List<String> onTabComplete(
            CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> playerNames = new ArrayList<>();

        if (strings.length == 1 && COMMANDS_TO_COMPLETE.contains(strings[0])) {
            Bukkit.getOnlinePlayers().forEach(a -> playerNames.add(a.getName()));
            return playerNames;
        } else if (strings.length == 2 && COMMANDS_TO_COMPLETE.contains(strings[0])) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().startsWith(strings[1])) {
                    playerNames.add(p.getName());
                }
            }
            return playerNames;
        }
        return playerNames;
    }
}
