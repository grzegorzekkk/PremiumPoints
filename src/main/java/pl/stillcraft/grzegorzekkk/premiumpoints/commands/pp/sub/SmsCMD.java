package pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.stillcraft.grzegorzekkk.premiumpoints.PremiumPoints;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.SubCMD;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.hooks.PlayerPointsHook;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Locale;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.Messenger;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDaoMysql;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.sms.SmsProvider;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.sms.SmsService;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.HikariPool;

import java.util.HashMap;
import java.util.Map;

public class SmsCMD implements SubCMD {

    @Override
    public boolean needsPlayer() {
        return true;
    }

    @Override
    public String getPermission() {
        return "sms";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        Map<Integer, SmsService> servicesMap = new HashMap<>();
        ConfigStorage.getInstance()
                .getProviderServices()
                .forEach(a -> servicesMap.put(a.getPoints(), a));

        int ppAmount = 0;

        if (args.length >= 2) {
            try {
                ppAmount = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
                e.getMessage();
                Messenger.send(p, Locale.PLUGIN_HEADER, Locale.SMS_INFO);
                return;
            }
        }

        if (args.length == 2 && servicesMap.containsKey(ppAmount)) {
            SmsService smsService = servicesMap.get(ppAmount);

            String msg =
                    MessageStorage.getInstance()
                            .getMessage(Locale.SMS_INFO_CHOICE)
                            .replaceAll("%pp%", String.valueOf(smsService.getPoints()))
                            .replaceAll("%message%", smsService.getPrefix())
                            .replaceAll("%number%", String.valueOf(smsService.getNumber()))
                            .replaceAll("%cost%", String.valueOf(smsService.getPrice()));

            Messenger.send(p, msg);
        } else if (args.length == 3 && servicesMap.containsKey(ppAmount)) {
            String smsCode = args[2];
            SmsService smsService = servicesMap.get(ppAmount);

            if (smsCode.length() != 8 || !smsCode.chars().allMatch(Character::isLetter)) {
                Messenger.send(p, Locale.PLUGIN_HEADER, Locale.SMS_FAILED);
                return;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (SmsProvider.isCodeValid(smsCode, smsService)) {
                        double multiplier = ConfigStorage.getInstance().getPpMultiplier();
                        int givePoints = (int) Math.round(multiplier * smsService.getPoints());

                        PlayerPointsHook.givePoints(p, givePoints);

                        PaymentDao pDao =
                                new PaymentDaoMysql(HikariPool.getInstance().getDataSource());
                        pDao.addPayment(p.getName(), givePoints, false);

                        Messenger.send(p, Locale.PLUGIN_HEADER);
                        Messenger.send(
                                p,
                                MessageStorage.getInstance()
                                        .getMessage(Locale.SMS_SUCCESSFULL)
                                        .replaceAll("%pp%", String.valueOf(givePoints)));

                        Messenger.broadcast(
                                MessageStorage.getInstance()
                                        .getMessage(Locale.SMS_SUCC_BROADCAST)
                                        .replaceAll("%pp%", String.valueOf(givePoints))
                                        .replaceAll("%player%", p.getName()));
                    } else {
                        Messenger.send(p, Locale.PLUGIN_HEADER, Locale.SMS_FAILED);
                    }
                }
            }.runTaskAsynchronously(PremiumPoints.getInstance());

        } else {
            Messenger.send(p, Locale.PLUGIN_HEADER, Locale.SMS_INFO);
        }
    }
}
