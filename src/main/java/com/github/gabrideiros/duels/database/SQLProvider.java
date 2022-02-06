package com.github.gabrideiros.duels.database;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.SQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class SQLProvider {

    private final Plugin plugin;

    public SQLConnector setup(final String forceType) {

        final FileConfiguration configuration = this.plugin.getConfig();

        final ConfigurationSection databaseConfiguration = configuration.getConfigurationSection("database");

        final String sqlType = (forceType != null) ? forceType : databaseConfiguration.getString("type");

        final Logger logger = this.plugin.getLogger();

        SQLConnector sqlConnector;

        if (sqlType.equalsIgnoreCase("mysql")) {
            final ConfigurationSection mysqlSection = databaseConfiguration.getConfigurationSection("mysql");
            sqlConnector = this.mysqlDatabaseType(mysqlSection).connect();
            logger.info("Connection to the database (MySQL) was successful.");

            return sqlConnector;
        }

        if (!sqlType.equalsIgnoreCase("sqlite")) {
            logger.severe("The selected database type is not valid.");
            return null;
        }

        final ConfigurationSection sqliteSection = databaseConfiguration.getConfigurationSection("sqlite");
        sqlConnector = this.sqliteDatabaseType(sqliteSection).connect();
        logger.info("Connection to the database (SQLite) was successful.");
        logger.warning("We recommend using the MySQL database.");

        return sqlConnector;
    }

    private SQLDatabaseType sqliteDatabaseType(final ConfigurationSection section) {
        return SQLiteDatabaseType.builder().file(new File(this.plugin.getDataFolder(), section.getString("file"))).build();
    }

    private SQLDatabaseType mysqlDatabaseType(final ConfigurationSection section) {
        return MySQLDatabaseType.builder().address(section.getString("address")).username(section.getString("username")).password(section.getString("password")).database(section.getString("database")).build();
    }

    private SQLProvider(final Plugin plugin) {
        this.plugin = plugin;
    }

    public static SQLProvider of(final Plugin plugin) {
        return new SQLProvider(plugin);
    }
}
