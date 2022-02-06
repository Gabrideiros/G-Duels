package com.github.gabrideiros.duels.model;

import com.github.gabrideiros.duels.DuelsPlugin;
import com.github.gabrideiros.duels.task.DuelRunnable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Duel {

    private final Player player;
    private final Player adversary;
    private final Kit kit;
    private final List<Player> spectators = new ArrayList<>();
    @Setter
    private boolean started;

    private int duelRunnable;

    public void addSpectator(Player player) {

        for (Player online : Bukkit.getOnlinePlayers()) {

            if (this.spectators.contains(player)) continue;

            online.hidePlayer(player);
        }

        spectators.add(player);
    }
    public void removeSpectator(Player player) {

        Bukkit.getOnlinePlayers().forEach(player::showPlayer);

        spectators.remove(player);
    }

    public void start() {
        this.duelRunnable = Bukkit.getScheduler().runTaskTimer(DuelsPlugin.getPlugin(DuelsPlugin.class), new DuelRunnable(this), 0L, 20L).getTaskId();
    }
}