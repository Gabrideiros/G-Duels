package com.github.gabrideiros.duels.task;

import com.github.gabrideiros.duels.model.Duel;
import com.github.gabrideiros.duels.util.PlayerUtil;
import com.github.gabrideiros.duels.util.Title;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

@RequiredArgsConstructor
public class DuelRunnable implements Runnable {

    private final Duel duel;
    private int time = 5;

    @Override
    public void run() {

        if (this.time == 0) {

            duel.setStarted(true);

            Title title = new Title("§e§lDuel", "PVP released, good luck!");

            title.send(this.duel.getPlayer());
            title.send(this.duel.getAdversary());

            Bukkit.getScheduler().cancelTask(this.duel.getDuelRunnable());
        }

        String message = String.format("§eStarting duel in %s seconds!", this.time);

        PlayerUtil.sendMessage(
                this.duel.getPlayer(),
                Sound.ENTITY_PLAYER_LEVELUP,
                5f,
                message
        );

        PlayerUtil.sendMessage(
                this.duel.getAdversary(),
                Sound.ENTITY_PLAYER_LEVELUP,
                5f,
                message
        );

        this.time--;
    }
}
