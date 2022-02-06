package com.github.gabrideiros.duels.adapter;

import com.github.gabrideiros.duels.util.ItemBuilder;
import com.github.gabrideiros.duels.util.SkullCreator;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.gabrideiros.duels.util.StringUtil.colorize;


public class ItemIAdapter implements IAdapter<ItemStack> {

    @Override
    public ItemStack parse(ConfigurationSection section) {

        ItemStack material = section.contains("head") && !section.getString("head").isEmpty()
                ? SkullCreator.itemFromUrl(section.getString("head"))
                : new ItemStack(Objects.requireNonNull(Material.getMaterial(section.getString("material"))),
                Math.min(1, section.getInt("amount")), (short) section.getInt("data"));

        ItemBuilder itemBuilder = new ItemBuilder(material);

        if (section.contains("enchant")) {

            List<String> enchants = section.getStringList("enchants");

            enchants.forEach(enchant ->  {

                String[] split = enchant.split(":");

                String name = split[0];
                int level = Integer.parseInt(split[1]);

                itemBuilder.enchantment(Enchantment.getByName(name), level);
            });
        }

        if (section.contains("name")) itemBuilder.name(colorize(section.getString("name")));

        if (section.contains("lore")) itemBuilder.lore(colorize(section.getStringList("lore")));


        try {
            return itemBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ItemStack> parseList(ConfigurationSection section) {
        return null;
    }

    public Map<Integer, ItemStack> parseMap(ConfigurationSection section) {

        Map<Integer, ItemStack> map = new HashMap<>();

        for (String key : section.getKeys(false)) {

            ConfigurationSection itemsSection = section.getConfigurationSection(key);

            int slot = itemsSection.getInt("slot");

            ConfigurationSection itemSection = itemsSection.getConfigurationSection("item");

            ItemStack item = parse(itemSection);

            if (item != null) map.put(slot, item);
        }
        return map;
    }
}