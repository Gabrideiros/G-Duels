package com.github.gabrideiros.duels.model;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Data
@Builder
public class Kit {

    private String name;
    private ItemStack helmet;
    private ItemStack chestPlate;
    private ItemStack leggings;
    private ItemStack boots;
    private Map<Integer, ItemStack> items;

}
