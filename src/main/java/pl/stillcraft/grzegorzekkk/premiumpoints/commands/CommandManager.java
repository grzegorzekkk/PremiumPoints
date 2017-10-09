package pl.stillcraft.grzegorzekkk.premiumpoints.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub.InfoCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;

import java.util.HashMap;
import java.util.List;

public class CommandManager implements CommandExecutor {

    public static final String PERM_BEGIN = "pp.";

    private static HashMap<List<String>, SubCMD> commands = new HashMap<>();

    public static void addComand(List<String> cmds, SubCMD s) {
        commands.put(cmds, s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
            boolean match = false;

            for (List<String> s : commands.keySet()) {
                if (s.contains(args[0])) {
                    commands.get(s).runCommand(sender, cmd, label, args);
                    match = true;
                }
            }

            if (!match) {
                Messenger.send(sender, Locale.COMMAND_NOT_FOUND);
            }
        } else {
            new InfoCMD().runCommand(sender, cmd, label, args);
        }

        return true;
    }
}
