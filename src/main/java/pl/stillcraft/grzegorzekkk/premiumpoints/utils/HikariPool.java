package pl.stillcraft.grzegorzekkk.premiumpoints.utils;

import com.zaxxer.hikari.HikariDataSource;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.database.DatabaseSettings;

import javax.sql.DataSource;
import java.util.Map;

public class HikariPool {

    private static HikariPool instance;
    private HikariDataSource hds;

    private HikariPool() {
    }

    public static HikariPool getInstance() {
        if (instance == null) {
            instance = new HikariPool();
        }
        return instance;
    }

    /**
     * Prepare HikariCp connection datasource. In this case MySQL jdbc.
     */
    public void setupHikari() {
        Map<DatabaseSettings, String> dbData = ConfigStorage.getInstance().getDatabaseSettings();
        hds = new HikariDataSource();
        hds.setJdbcUrl(
                "jdbc:mysql://"
                        + dbData.get(DatabaseSettings.HOST)
                        + ":"
                        + dbData.get(DatabaseSettings.PORT)
                        + "/"
                        + dbData.get(DatabaseSettings.DB_NAME));
        hds.setUsername(dbData.get(DatabaseSettings.USERNAME));
        hds.setPassword(dbData.get(DatabaseSettings.PASSWORD));
        hds.addDataSourceProperty("cachePrepStmts", "true");
        hds.addDataSourceProperty("prepStmtCacheSize", "250");
        hds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    public DataSource getDataSource() {
        return hds;
    }

    /**
     * Closes hikaricp connection pool.
     */
    public void close() {
        hds.close();
        instance = null;
    }
}
