package com.github.gabrideiros.duels.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSReflections {

    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String PATH_NMS = "net.minecraft.server." + VERSION + ".";
    private static final String PATH_OBC = "org.bukkit.craftbukkit." + VERSION + ".";

    protected void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);

            playerConnection.getClass().getMethod("sendPacket",
                    getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Class<?> getNMSClass(String name) {
        try {
            return Class.forName(PATH_NMS + name);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    protected static Class<?> getOBCClass(String name) {
        try {
            return Class.forName(PATH_OBC + name);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(this.toString() + "." + className);
    }
}