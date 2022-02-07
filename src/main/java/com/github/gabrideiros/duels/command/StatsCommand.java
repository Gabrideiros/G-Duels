package com.github.gabrideiros.duels.command;

import com.github.gabrideiros.duels.database.storage.AccountStorage;
import com.github.gabrideiros.duels.model.Account;
import com.github.gabrideiros.duels.util.PlayerUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class StatsCommand implements CommandExecutor {

    private final AccountStorage accountStorage;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))  {
            sender.sendMessage("§cThis command is allowed only in-game.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cUse: §f/stats (player).");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cThis player was not found.");
            return true;
        }

        CompletableFuture.runAsync(() -> {
            Account account = this.accountStorage.findAccount(target);

            PlayerUtil.sendMessage(
                    (Player) sender,
                    Sound.LEVEL_UP,
                    5f,
                    "",
                    String.format("§e§l Status of %s:", target.getName()),
                    "",
                    String.format("§f Wins: §7%s.", account.getWins()),
                    String.format("§f Losses: §7%s.", account.getLoss()),
                    String.format("§f Kills: §7%s.", account.getKills()),
                    String.format("§f Deaths: §7%s.", account.getDeaths()),
                    ""
            );
        });
        return true;
    }
}
