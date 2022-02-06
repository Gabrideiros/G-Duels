package com.github.gabrideiros.duels.util;

import org.bukkit.*;

public class LocationUtil
{
    public static String serialize(final Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public static Location deserialize(final String location) {
        if (location == null) {
            return null;
        }
        final String[] split = location.split(",");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
