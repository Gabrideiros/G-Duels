package com.github.gabrideiros.duels.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerUtil {

    public static void sendMessage(Player player, Sound sound, float volume, String... message) {
        if (player != null) {
            player.sendMessage(message);

            if (sound != null) playSound(player, sound, volume);
        }
    }

    public static void playSound(Player player, Sound sound, float volume) {
        player.playSound(player.getLocation(), sound, volume, volume);
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        new Title(title, subTitle).send(player);
    }

    public static void clearEffects(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public static void clear(Player player) {
        player.setHealth(20D);
        player.setFoodLevel(20);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        clearEffects(player);
    }
}