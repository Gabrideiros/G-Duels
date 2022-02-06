package com.github.gabrideiros.duels.adapter;

import com.github.gabrideiros.duels.model.Kit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

import static com.github.gabrideiros.duels.util.StringUtil.colorize;

public class KitAdapter implements IAdapter<Kit> {

    private static final ItemIAdapter ADAPTER = new ItemIAdapter();

    @Override
    public Kit parse(ConfigurationSection section) {
        try {
            return Kit.builder()
                    .name(colorize(section.getName()))
                    .helmet(ADAPTER.parse(section.getConfigurationSection("armor_content.helmet")))
                    .chestPlate(ADAPTER.parse(section.getConfigurationSection("armor_content.chestplate")))
                    .leggings(ADAPTER.parse(section.getConfigurationSection("armor_content.leggings")))
                    .boots(ADAPTER.parse(section.getConfigurationSection("armor_content.boots")))
                    .items(ADAPTER.parseMap(section.getConfigurationSection("inventory_content")))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Kit> parseList(ConfigurationSection section) {

        List<Kit> list = new ArrayList<>();

        for (String key : section.getKeys(false)) {

            Kit kit = parse(section.getConfigurationSection(key));

            if (kit != null) list.add(kit);
        }
        return list;
    }
}
