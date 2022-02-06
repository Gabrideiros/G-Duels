package com.github.gabrideiros.duels.listener;

import com.github.gabrideiros.duels.model.Duel;
import com.github.gabrideiros.duels.service.DuelService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class DuelListener implements Listener {

    private final DuelService duelService;

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        duelService.closeBattle(player, false);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (duelService.closeBattle(player, true)) event.getDrops().clear();
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Duel duel = this.duelService.getBattle(player).orElse(null);

        if (duel == null) return;

        if (!duel.isStarted()) player.teleport(player.getLocation());
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();

        Duel duel = this.duelService.getBattle(player).orElse(null);

        if (duel == null) return;

        if (!duel.isStarted()) event.setCancelled(true);
    }
}
