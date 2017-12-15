package pl.stillcraft.grzegorzekkk.premiumpoints.configuration;

import org.bukkit.configuration.ConfigurationSection;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.database.DatabaseSettings;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.email.EmailSettings;
import pl.stillcraft.grzegorzekkk.premiumpoints.payments.sms.SmsService;
import pl.stillcraft.grzegorzekkk.premiumpoints.utils.Storage;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Container class used to store Configuration entries of plugin.
 */
public class ConfigStorage extends Storage {

    private static ConfigStorage instance;

    private ConfigStorage() {
    }

    public static ConfigStorage getInstance() {
        if (instance == null) {
            instance = new ConfigStorage();
        }
        return instance;
    }

    public String getProviderURL() {
        return config.getString("provider.url");
    }

    public String getValidCodeResponse() {
        return config.getString("provider.resp_valid");
    }

    public List<SmsService> getProviderServices() {
        List<SmsService> services = new ArrayList<>();

        ConfigurationSection cs = config.getConfigurationSection("provider.services");

        for (String key : cs.getKeys(false)) {
            services.add(
                    new SmsService(
                            cs.getInt(key + ".points"),
                            cs.getInt(key + ".number"),
                            cs.getString(key + ".prefix"),
                            cs.getDouble(key + ".price")));
        }

        return services;
    }

    public double getPpMultiplier() {
        return config.getDouble("pp_multiplier");
    }

    public String getSecret() {
        return config.getString("secret");
    }

    public Map<DatabaseSettings, String> getDatabaseSettings() {
        Map<DatabaseSettings, String> databaseData = new HashMap<>();

        for (DatabaseSettings setting : DatabaseSettings.values()) {
            databaseData.put(setting, config.getString(setting.getKey()));
        }
        return databaseData;
    }

    public Map<EmailSettings, String> getEmailSettings() {
        Map<EmailSettings, String> emailSettingsMap = new HashMap<>();

        for (EmailSettings setting : EmailSettings.values()) {
            emailSettingsMap.put(setting, config.getString(setting.getKey()));
        }
        return emailSettingsMap;
    }
}
