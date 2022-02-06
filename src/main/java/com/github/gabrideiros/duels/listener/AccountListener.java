package com.github.gabrideiros.duels.listener;

import com.github.gabrideiros.duels.database.storage.AccountStorage;
import com.github.gabrideiros.duels.model.Account;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class AccountListener implements Listener {

    private final AccountStorage accountStorage;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        CompletableFuture.runAsync(() -> this.accountStorage.put(this.accountStorage.findAccount(player)));
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final Account account = this.accountStorage.findAccount(player);

        CompletableFuture.runAsync(() -> this.accountStorage.getAccountRepository().saveOne(account));

        this.accountStorage.getCache().remove(player.getUniqueId());
    }
}
