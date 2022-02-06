package com.github.gabrideiros.duels.database.storage;

import com.github.gabrideiros.duels.database.repository.AccountRepository;
import com.github.gabrideiros.duels.model.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@RequiredArgsConstructor
public final class AccountStorage {

    private final AccountRepository accountRepository;

    private final Map<UUID, Account> cache = new HashMap<>();

    public void init() {
        accountRepository.createTable();
    }

    public void saveOne(Account account) {
        accountRepository.saveOne(account);
    }

    public Account selectOne(Player player) {

        Account account = accountRepository.selectOne(player.getUniqueId().toString());

        if (account == null) account = Account.builder()
                .uuid(player.getUniqueId())
                .wins(0)
                .loss(0)
                .kills(0)
                .deaths(0)
                .build();

        return account;
    }

    public Account findAccountByUuid(UUID uuid) {
        return cache.get(uuid);
    }

    public Account findAccount(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) return findAccount(player);
        }

        return findAccountByUuid(offlinePlayer.getUniqueId());
    }

    public Account findAccount(Player player) {

        Account account = findAccountByUuid(player.getUniqueId());

        if (account == null) account = selectOne(player);

        return account;
    }

    public void put(Account account) {
        cache.put(account.getUuid(), account);
    }

    private static final ExecutorService executor = Executors.newFixedThreadPool(128);

    public void flushData() {
        for (Account entry : cache.values()) {
            executor.execute(() -> {
                accountRepository.saveOne(entry);
            });
        }
    }
}