package com.github.gabrideiros.duels.factory;

import com.github.gabrideiros.duels.adapter.KitAdapter;
import com.github.gabrideiros.duels.model.Kit;
import com.github.gabrideiros.duels.util.Registry;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class KitFactory extends Registry<Kit> {

    private static final KitAdapter ADAPTER = new KitAdapter();

    public Kit getByName(String name) {
        return get(kit -> kit.getName().equalsIgnoreCase(name));
    }

    public void register(FileConfiguration configuration) {
        ADAPTER.parseList(Objects.requireNonNull(configuration.getConfigurationSection("kits"))).forEach(this::add);
    }
}
