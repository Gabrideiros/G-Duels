package com.github.gabrideiros.duels.util;

import com.github.gabrideiros.duels.util.NMSReflections;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import static com.github.gabrideiros.duels.util.StringUtil.colorize;

public class Title extends NMSReflections {

    private final JSONObject title, subtitle;
    private final int fadeIn, fadeOut, stay;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = convert(title);
        this.subtitle = convert(subtitle);
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    public Title(JSONObject title, JSONObject subtitle, int fadeIn, int fadeOut, int stay) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    public Title(String title, String subtitle) {
        this.title = convert(title);
        this.subtitle = convert(subtitle);
        this.fadeIn = 3;
        this.fadeOut = 80;
        this.stay = 5;
    }

    @SuppressWarnings("unchecked")
    static JSONObject convert(String text) {
        JSONObject json = new JSONObject();
        json.put("text", text);

        return json;
    }

    public void send(Player player) {
        Preconditions.checkNotNull(player);
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            Class<?> clsPacketPlayOutTitle = getNMSClass("PacketPlayOutTitle");
            Class<?> clsPacket = getNMSClass("Packet");
            Class<?> clsIChatBaseComponent = getNMSClass("IChatBaseComponent");
            Class<?> clsChatSerializer = getNMSClass("IChatBaseComponent$ChatSerializer");
            Class<?> clsEnumTitleAction = getNMSClass("PacketPlayOutTitle$EnumTitleAction");

            Object timesPacket = clsPacketPlayOutTitle.getConstructor(int.class, int.class, int.class)
                    .newInstance(fadeIn, stay, fadeOut);

            playerConnection.getClass().getMethod("sendPacket", clsPacket).invoke(playerConnection, timesPacket);

            if (title != null && !title.isEmpty()) {
                Object titleComponent = clsChatSerializer.getMethod("a", String.class)
                        .invoke(null, StringUtil.colorize(title.toString()));
                Object titlePacket = clsPacketPlayOutTitle.getConstructor(clsEnumTitleAction, clsIChatBaseComponent)
                        .newInstance(clsEnumTitleAction.getField("TITLE").get(null), titleComponent);
                playerConnection.getClass().getMethod("sendPacket", clsPacket)
                        .invoke(playerConnection, titlePacket);
            }

            if (subtitle != null && !subtitle.isEmpty()) {
                Object subtitleComponent = clsChatSerializer.getMethod("a", String.class)
                        .invoke(null, colorize(subtitle.toString()));
                Object subtitlePacket = clsPacketPlayOutTitle.getConstructor(clsEnumTitleAction, clsIChatBaseComponent)
                        .newInstance(clsEnumTitleAction.getField("SUBTITLE").get(null), subtitleComponent);
                playerConnection.getClass().getMethod("sendPacket", clsPacket)
                        .invoke(playerConnection, subtitlePacket);
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void sendToAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(player);
        }
    }
}