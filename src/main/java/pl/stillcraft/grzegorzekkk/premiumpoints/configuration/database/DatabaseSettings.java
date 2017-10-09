package pl.stillcraft.grzegorzekkk.premiumpoints.configuration.database;

public enum DatabaseSettings {
    HOST("database.host"),
    DB_NAME("database.database_name"),
    PORT("database.port"),
    USERNAME("database.username"),
    PASSWORD("database.password");

    private String key;

    DatabaseSettings(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
