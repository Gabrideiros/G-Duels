package com.github.gabrideiros.duels;

import com.github.gabrideiros.duels.command.AcceptCommand;
import com.github.gabrideiros.duels.command.DuelCommand;
import com.github.gabrideiros.duels.command.StatsCommand;
import com.github.gabrideiros.duels.database.SQLProvider;
import com.github.gabrideiros.duels.database.repository.AccountRepository;
import com.github.gabrideiros.duels.database.storage.AccountStorage;
import com.github.gabrideiros.duels.factory.KitFactory;
import com.github.gabrideiros.duels.listener.AccountListener;
import com.github.gabrideiros.duels.listener.DuelListener;
import com.github.gabrideiros.duels.service.DuelService;
import com.github.gabrideiros.duels.util.CustomConfig;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.Getter;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class DuelsPlugin extends JavaPlugin {

    private AccountStorage accountStorage;

    private CustomConfig kitsStorage;

    private CustomConfig locationsStorage;

    private DuelService duelService;

    private KitFactory kitFactory;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        initConfigs();

        var sqlConnector = SQLProvider.of(this).setup(null);
        var sqlExecutor = new SQLExecutor(sqlConnector);
        var accountRepository = new AccountRepository(sqlExecutor);

        this.accountStorage = new AccountStorage(accountRepository);

        this.accountStorage.init();

        this.duelService = new DuelService(this);

        this.kitFactory = new KitFactory();
        this.kitFactory.register(this.kitsStorage.getConfig());

        registerListeners(
                new AccountListener(this.accountStorage),
                new DuelListener(this.duelService)
        );

        registerCommands();
    }

    @Override
    public void onDisable() {
        this.accountStorage.flushData();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    private void initConfigs() {
        this.kitsStorage = new CustomConfig(this, getDataFolder().getPath(), "kits.yml");
        this.kitsStorage.saveDefaultConfig();

        this.locationsStorage = new CustomConfig(this, getDataFolder().getPath(), "locations.yml");
        this.locationsStorage.saveDefaultConfig();
    }

    private void registerCommands() {
        getCommand("duel").setExecutor(new DuelCommand(this.duelService, this.kitFactory, this.locationsStorage));
        getCommand("stats").setExecutor(new StatsCommand(this.accountStorage));
        getCommand("accept").setExecutor(new AcceptCommand(this.duelService));
    }
}
