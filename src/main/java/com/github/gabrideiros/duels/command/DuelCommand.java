package com.github.gabrideiros.duels.command;

import com.github.gabrideiros.duels.factory.KitFactory;
import com.github.gabrideiros.duels.model.Duel;
import com.github.gabrideiros.duels.model.Kit;
import com.github.gabrideiros.duels.service.DuelService;
import com.github.gabrideiros.duels.util.CustomConfig;
import com.github.gabrideiros.duels.util.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DuelCommand implements CommandExecutor {

    private final DuelService duelService;
    private final KitFactory kitFactory;
    private final CustomConfig locationStorage;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))  {
            sender.sendMessage("§cThis command is allowed only in-game.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1) {

            String arg = args[0].toLowerCase();

            switch (arg) {
                case "pos1":
                case "pos2":
                case "back":
                    sender.sendMessage(String.format("§aLocation %s set!", arg));
                    this.duelService.setLocation(arg, player.getLocation());
                    this.locationStorage.getConfig().set(arg, LocationUtil.serialize(player.getLocation()));
                    this.locationStorage.save();
                    this.locationStorage.reload();
                    return true;
            }
        }

        if (args.length < 2) {
            sender.sendMessage("§cUse: §f/duel (player) (kit).");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cThis player was not found.");
            return true;
        }

        Kit kit = kitFactory.getByName(args[1]);

        if (kit == null) {
            sender.sendMessage("§cThis kit was not found.");
            return true;
        }

        if (this.duelService.hasBattle(player)) {
            sender.sendMessage("§cYou are already in a duel.");
            return true;
        }

        if (this.duelService.hasBattle(target)) {
            sender.sendMessage("§cThis player is already in a duel.");
            return true;
        }

        this.duelService.getInvite().put(
                target,
                new Duel(
                        target,
                        player,
                        kit
                )
        );
        return true;
    }
}
