package pl.stillcraft.grzegorzekkk.premiumpoints;

import org.bukkit.plugin.java.JavaPlugin;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.CommandManager;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.PlayerTabCompleter;
import pl.stillcraft.grzegorzekkk.premiumpoints.commands.pp.sub.*;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.hooks.PlayerPointsHook;
import pl.stillcraft.grzegorzekkk.premiumpoints.messages.MessageStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDao;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.PaymentDaoMysql;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.psc.CollectPpTask;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.ConsoleLogger;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.HikariPool;

import java.util.Arrays;

public class PremiumPoints extends JavaPlugin {

    private static PremiumPoints instance;

    public static PremiumPoints getInstance() {
        return instance;
    }

    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
        instance = this;
        ConsoleLogger.setLogger(getLogger());

        MessageStorage.getInstance().setConfigFilename("messages.yml");
        MessageStorage.getInstance().reloadConfig();

        ConfigStorage.getInstance().setConfigFilename("config.yml");
        ConfigStorage.getInstance().reloadConfig();

        HikariPool.getInstance().setupHikari();

        PaymentDao pDao = new PaymentDaoMysql(HikariPool.getInstance().getDataSource());
        pDao.createTable();

        CollectPpTask task = CollectPpTask.getInstance();
        task.startCollecting();
        task.startPlayerInforming();

        ConsoleLogger.info("Enabled PremiumPoints plugin!");
        if (PlayerPointsHook.hookPlayerPoints()) {
            ConsoleLogger.info("Hooked PlayerPoints");
        }
        ConsoleLogger.info("Plugin created by grzegorzekkk");

        registerCommands();
    }

    // Fired when plugin is disabled
    @Override
    public void onDisable() {
        HikariPool.getInstance().close();
        ConsoleLogger.info("PremiumPoints plugin has been disabled");
    }

    private void registerCommands() {
        getCommand("pp").setExecutor(new CommandManager());
        getCommand("pp").setTabCompleter(new PlayerTabCompleter());

        CommandManager.addComand(Arrays.asList("reload", "r"), new ReloadCMD());
        CommandManager.addComand(Arrays.asList("info"), new InfoCMD());
        CommandManager.addComand(Arrays.asList("sms"), new SmsCMD());
        CommandManager.addComand(Arrays.asList("history", "historia"), new HistoryCMD());
        CommandManager.addComand(Arrays.asList("top"), new TopCMD());
        CommandManager.addComand(Arrays.asList("givepsc"), new GivePscCMD());
        CommandManager.addComand(Arrays.asList("psc"), new PscCMD());
        CommandManager.addComand(Arrays.asList("collect", "odbierz"), new CollectCMD());
        CommandManager.addComand(Arrays.asList("pay", "daj"), new PayCMD());
        CommandManager.addComand(Arrays.asList("add"), new AddCMD());
    }
}
