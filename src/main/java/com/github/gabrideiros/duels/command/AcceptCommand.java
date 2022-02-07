package com.github.gabrideiros.duels.command;

import com.github.gabrideiros.duels.model.Duel;
import com.github.gabrideiros.duels.service.DuelService;
import com.github.gabrideiros.duels.util.PlayerUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AcceptCommand implements CommandExecutor {

    private final DuelService duelService;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))  {
            sender.sendMessage("§cThis command is allowed only in-game.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cUse: §f/accept (player).");
            return true;
        }

        Player player = (Player) sender;

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cThis player was not found.");
            return true;
        }

        if (!this.duelService.getInvite().containsKey(player)) {
            PlayerUtil.sendMessage(
                    player,
                    Sound.VILLAGER_NO,
                    5f,
                    "§cYou don't have any pending invites."
            );
            return true;
        }

        Duel duel = this.duelService.getInvite().get(player);

        if (!duel.getAdversary().equals(target)) {
            PlayerUtil.sendMessage(
                    player,
                    Sound.VILLAGER_NO,
                    5f,
                    "§cThis player has not invited."
            );
            return  true;
        }

        this.duelService.getDuels().add(duel);
        this.duelService.getInvite().removeFromMap(player);
        return true;
    }
}
