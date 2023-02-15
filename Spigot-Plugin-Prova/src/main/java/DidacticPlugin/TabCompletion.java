package DidacticPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String nomeComando, @NotNull String[] argomenti) {

        if (command.getName().equalsIgnoreCase("Soldi")) {
            if (sender instanceof Player p) {

                List<String> list = new ArrayList<>();
                Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();

                if (argomenti.length == 1) {
                    if (p.hasPermission("didacticplugin.setsoldi") || p.hasPermission("didacticplugin.*")) {
                        list.add("set");
                    }
                    if (!argomenti[0].startsWith("s")) {
                        for (Player t : onlinePlayers) {
                            list.add(t.getName());
                        }
                    }
                } else if (argomenti.length == 2 && argomenti[0].equalsIgnoreCase("set")) {
                    for(Player t : onlinePlayers) {
                        list.add(t.getName());
                    }
                }

                return list;
            }
        }
        return null;
    }
}
