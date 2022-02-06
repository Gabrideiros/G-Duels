package com.github.gabrideiros.duels.util;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.repeat;

public class StringUtil {

    private static final char[] ALPHA_NUMERIC_CHARS;
    private static final ChatColor[] COLORS;

    static {
        COLORS = new ChatColor[]{
                ChatColor.AQUA,
                ChatColor.BLACK,
                ChatColor.BLUE,
                ChatColor.GOLD,
                ChatColor.GRAY,
                ChatColor.GREEN,
                ChatColor.LIGHT_PURPLE,
                ChatColor.RED,
                ChatColor.YELLOW,
                ChatColor.WHITE,
                ChatColor.DARK_AQUA,
                ChatColor.DARK_BLUE,
                ChatColor.DARK_GRAY,
                ChatColor.DARK_GREEN,
                ChatColor.DARK_PURPLE,
                ChatColor.DARK_RED,
        };
        ALPHA_NUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public static String colorize(String input) {
        return input.replace("&", "§");
    }

    public static String[] colorize(String... lore) {
        for (int i = 0; i < lore.length; i++) lore[i] = colorize(lore[i]);

        return lore;
    }

    public static List<String> colorize(List<String> lore) {
        return lore.stream().map(StringUtil::colorize).collect(Collectors.toList());
    }

    public static String rainbow(String input) {
        final StringBuilder builder = new StringBuilder();

        int i = 0;
        for (char c : input.toCharArray()) {
            builder.append(COLORS[i]).append(c);

            i++;

            i = (i + COLORS.length) % COLORS.length;
        }

        return builder.toString();
    }

    public static String progressBar(int current, int total, String bar, int amount, ChatColor[] completeIncomplete) {
        float percent = (float) current / total;

        if (percent > 1) percent = 1;

        int bars1 = Math.round(amount * percent);
        int bars2 = amount - bars1;

        return completeIncomplete[0] + repeat(bar, bars1) + completeIncomplete[1] + repeat(bar, bars2);
    }
}