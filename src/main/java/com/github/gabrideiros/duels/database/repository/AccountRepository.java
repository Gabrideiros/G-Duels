package com.github.gabrideiros.duels.database.repository;

import com.github.gabrideiros.duels.database.adapter.AccountAdapter;
import com.github.gabrideiros.duels.model.Account;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class AccountRepository {

    private static final String TABLE = "duels_accounts";

    private final SQLExecutor sqlExecutor;

    public void createTable() {
        this.sqlExecutor.updateQuery(String.format("CREATE TABLE IF NOT EXISTS %s (uuid CHAR(36) NOT NULL PRIMARY KEY, wins INT NOT NULL, loss INT NOT NULL, kills INT NOT NULL, deaths INT NOT NULL);", TABLE));
    }

    private Account selectOneQuery(final String query) {
        return this.sqlExecutor.resultOneQuery(String.format("SELECT * FROM %s " + query, TABLE), statement -> {
        }, AccountAdapter.class);
    }

    public Account selectOne(final String uuid) {
        return this.selectOneQuery("WHERE uuid = '" + uuid + "'");
    }

    public Set<Account> selectAll(final String query) {
        return this.sqlExecutor.resultManyQuery(String.format("SELECT * FROM %s " + query, TABLE), k -> {
        }, AccountAdapter.class);
    }

    public void saveOne(final Account account) {
        this.sqlExecutor.updateQuery(String.format("REPLACE INTO %s VALUES(?,?,?,?,?)", TABLE), statement -> {
            statement.set(1, account.getUuid().toString());
            statement.set(2, account.getWins());
            statement.set(3, account.getLoss());
            statement.set(4, account.getKills());
            statement.set(5, account.getDeaths());
        });
    }

    public void updateOne(final Account account) {
        this.sqlExecutor.updateQuery(String.format("UPDATE %s SET wins=?, loss=?, kills=?, deaths=? WHERE uuid=?", TABLE), statement -> {
            statement.set(1, account.getWins());
            statement.set(2, account.getLoss());
            statement.set(3, account.getKills());
            statement.set(4, account.getDeaths());
            statement.set(5, account.getUuid().toString());
        });
    }
}
