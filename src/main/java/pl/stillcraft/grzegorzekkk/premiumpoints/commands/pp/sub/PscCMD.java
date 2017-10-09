package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.email.EmailSettings;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;

import java.util.Map;

public class PscCMD implements SubCMD {

    @Override
    public boolean needsPlayer() {
        return true;
    }

    @Override
    public String getPermission() {
        return "psc";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (args.length == 2) {
            String pscCode = args[1];
            if (pscCode.length() != 16 || !pscCode.chars().allMatch(Character::isDigit)) {
                Messenger.send(p, Locale.PLUGIN_HEADER, Locale.PSC_INVALID);
            } else {
                Messenger.send(p, Locale.PLUGIN_HEADER, Locale.PSC_VALID);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        sendMail(p, pscCode);
                    }
                }.runTaskAsynchronously(PremiumPoints.getInstance());
            }
        } else {
            Messenger.send(p, Locale.PLUGIN_HEADER, Locale.PSC_INFO);
        }
    }

    private void sendMail(final Player p, final String codePaysafecard) {
        Map<EmailSettings, String> m = ConfigStorage.getInstance().getEmailSettings();
        try {
            Email email = new SimpleEmail();
            email.setHostName(m.get(EmailSettings.SMTP));
            email.setSmtpPort(Integer.parseInt(m.get(EmailSettings.PORT)));
            email.setAuthenticator(
                    new DefaultAuthenticator(
                            m.get(EmailSettings.USERNAME), m.get(EmailSettings.PASSWORD)));
            email.setSSLOnConnect(true);
            email.setCharset("UTF-8");
            email.setFrom(m.get(EmailSettings.USERNAME));
            email.setSubject(p.getName() + " Paysafecard");
            email.setMsg(p.getName() + " " + codePaysafecard);
            email.addTo(m.get(EmailSettings.RECEIVER));
            email.send();
        } catch (EmailException e) {
            e.getMessage();
        }
    }
}
