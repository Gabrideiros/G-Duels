package com.github.gabrideiros.duels.service;

import com.github.gabrideiros.duels.DuelsPlugin;
import com.github.gabrideiros.duels.model.Account;
import com.github.gabrideiros.duels.model.Duel;
import com.github.gabrideiros.duels.model.Kit;
import com.github.gabrideiros.duels.util.LocationUtil;
import com.github.gabrideiros.duels.util.PlayerUtil;
import com.github.gabrideiros.duels.util.TemporaryHashMap;
import com.github.gabrideiros.duels.util.Title;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DuelService {

    private Location pos1;
    private Location pos2;
    private Location back;

    @Getter
    private final List<Duel> duels;

    private final DuelsPlugin plugin;

    @Getter
    TemporaryHashMap<Player, Duel> invite;

    public DuelService(DuelsPlugin plugin) {

        this.plugin = plugin;

        FileConfiguration config = plugin.getLocationsStorage().getConfig();

        this.pos1 = LocationUtil.deserialize(config.getString("pos1"));
        this.pos2 =  LocationUtil.deserialize(config.getString("pos2"));
        this.back =  LocationUtil.deserialize(config.getString("back"));

        this.duels = new ArrayList<>();

        initInvite();
    }

    public void createBattle(Duel duel) {

        Player player = duel.getPlayer();
        Player adversary = duel.getAdversary();

        Kit kit = duel.getKit();

        this.setKit(player, kit);
        this.setKit(adversary, kit);

        player.teleport(this.pos1);
        adversary.teleport(this.pos2);

        duel.start();
    }

    private void setKit(Player player, Kit kit) {

        PlayerInventory inventory = player.getInventory();

        PlayerUtil.clear(player);

        inventory.setHelmet(kit.getHelmet());
        inventory.setChestplate(kit.getChestPlate());
        inventory.setLeggings(kit.getLeggings());
        inventory.setBoots(kit.getHelmet());

        kit.getItems().forEach(inventory::setItem);
    }

    public boolean closeBattle(Player player, boolean death) {

        Duel duel = this.getBattle(player).orElse(null);

        if (duel == null) return false;

        Player adversary = duel.getPlayer().equals(player) ? duel.getAdversary() : duel.getPlayer();

        Account account = plugin.getAccountStorage().findAccount(player);
        Account AdversaryAccount = plugin.getAccountStorage().findAccount(adversary);

        account.setLoss(account.getLoss() + 1);
        AdversaryAccount.setWins(AdversaryAccount.getWins() + 1);

        Title title = new Title("§2Win", String.format("§fYou won the duel against §7%s", player.getName()));

        title.send(adversary);

        adversary.teleport(this.back);

        this.duels.remove(duel);

        PlayerUtil.clear(player);
        PlayerUtil.clear(adversary);

        if (death) {

            account.setDeaths(account.getDeaths() + 1);
            AdversaryAccount.setKills(AdversaryAccount.getKills() + 1);

            title = new Title("§4Defeat", String.format("§fYou lost the duel against §7%s", adversary.getName()));

            title.send(player);

            player.teleport(this.back);
        }
        return true;
    }
    
    public void setLocation(String arg, Location location) {
        switch (arg) {
            case "pos1":
                this.pos1 = location;
            case "pos2":
                this.pos2 = location;
            case "back":
                this.back = location;
        }
    }

    public Optional<Duel> getBattle(Player player) {
        return getBattle(player.getUniqueId());
    }

    public Optional<Duel> getBattle(UUID uuid) {
        return this.duels.stream().filter(duel -> duel.getPlayer().getUniqueId().equals(uuid) || duel.getAdversary().getUniqueId().equals(uuid)).findAny();
    }

    public boolean hasBattle(Player player) {
        return hasBattle(player.getUniqueId());
    }

    public boolean hasBattle(UUID uuid) {
        return duels.stream().anyMatch(duel -> duel.getPlayer().getUniqueId().equals(uuid) || duel.getAdversary().getUniqueId().equals(uuid));
    }

    public void initInvite() {
        this.invite = new TemporaryHashMap<>(TimeUnit.SECONDS.toMillis(30), new TemporaryHashMap.EntryCallback<Player, Duel>() {

            @Override
            public void onAdd(Player key, Duel value) {

                Player adversary = value.getAdversary();

                PlayerUtil.sendMessage(
                        adversary,
                        Sound.LEVEL_UP,
                        5f,
                        String.format("§eYou invited %s to a duel.", key.getName())
                );

                PlayerUtil.sendMessage(
                        key,
                        Sound.LEVEL_UP,
                        5f,
                        String.format("§e%s invited you to a duel.", adversary.getName())
                );
            }

            @Override
            public void onRemove(Player key, Duel value) {

                Player adversary = value.getAdversary();

                if (hasBattle(key)) {
                    PlayerUtil.sendMessage(
                            adversary,
                            Sound.LEVEL_UP,
                            5f,
                            String.format("§e%s accepted your duel request.", key.getName())
                    );

                    PlayerUtil.sendMessage(
                            key,
                            Sound.LEVEL_UP,
                            5f,
                            String.format("§eYou accepted the duel of %s.", adversary.getName())
                    );

                    createBattle(value);
                    return;
                }

                PlayerUtil.sendMessage(
                        adversary,
                        Sound.LEVEL_UP,
                        5f,
                        String.format("§e%s refused his request for a duel.", key.getName())
                );

                PlayerUtil.sendMessage(
                        key,
                        Sound.LEVEL_UP,
                        5f,
                        String.format("§eYou refused the duel of %s.", adversary.getName())
                );
            }
        });
    }
}
