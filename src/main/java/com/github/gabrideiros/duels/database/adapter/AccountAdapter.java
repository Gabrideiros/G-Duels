package com.github.gabrideiros.duels.database.adapter;

import com.github.gabrideiros.duels.model.Account;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;

import java.util.UUID;

public class AccountAdapter implements SQLResultAdapter<Account> {

    @Override
    public Account adaptResult(final SimpleResultSet resultSet) {

        final String uuid = resultSet.get("uuid");
        final int wins = resultSet.get("wins");
        final int loss = resultSet.get("loss");
        final int kills = resultSet.get("kills");
        final int deaths = resultSet.get("deaths");

        return Account.builder()
                .uuid(UUID.fromString(uuid))
                .wins(wins)
                .loss(loss)
                .kills(kills)
                .deaths(deaths)
                .build();
    }
}
