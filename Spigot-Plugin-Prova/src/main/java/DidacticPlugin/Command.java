package DidacticPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String nomeComando, String[] argomenti) {

        if (command.getName().equalsIgnoreCase("MyCommand")) {
            if (sender instanceof Player p) {
                if (argomenti.length == 1)
                    p.sendMessage(p.getName() + " sei un coglione...");
                else if (argomenti.length == 2 && argomenti[0].equalsIgnoreCase("tp_serializing_for_player") && p.hasPermission(argomenti[1])) {
                    Permissions permessi = MyPlugin.get_permissions();
                    permessi.setPermissions(p, argomenti[1], false);
                    Location death = p.getLastDeathLocation();
                    assert death != null;
                    p.teleport(death);
                }
            } else {
                sender.sendMessage("§4Devi essere un player lol");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("Soldi")) {
            Economy economia = new Economy();

            if (argomenti.length == 0) {
                if (sender instanceof Player p) {
                    String uuid = p.getUniqueId().toString();
                    String mess = MyPlugin.getInstance().getConfig().getString("Economia.messaggioSoldi");
                    assert mess != null;
                    p.sendMessage(mess.replace("%money", economia.getSoldi(uuid).toString()).replaceAll("&", "§"));
                } else {
                    sender.sendMessage("§4Devi essere un player lol");
                    sender.sendMessage("§4Oppure: /soldi <nome_player>");
                }
            }
            else if (argomenti.length == 1) {
                Player target = Bukkit.getPlayer(argomenti[0]);
                if (target != null) {
                    String uuid = target.getUniqueId().toString();
                    String mess = MyPlugin.getInstance().getConfig().getString("Economia.messaggioSoldiTarget");
                    assert mess != null;
                    sender.sendMessage(mess.replace("%money", economia.getSoldi(uuid).toString()).replaceAll("&", "§").replace("%user",target.getName()));
                } else {
                    sender.sendMessage("§4Uso: /soldi <nome_player>");
                }
            }
            else if (argomenti.length == 3 && argomenti[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("didacticplugin.setsoldi") || sender.hasPermission("didacticplugin.*")) {
                    Player target = Bukkit.getPlayer(argomenti[1]);
                    if (target != null) {
                        String uuid = target.getUniqueId().toString();
                        int s_nuovi;
                        try {
                            if (argomenti[2].length() > 10 && argomenti[2].startsWith("-")) s_nuovi = -2147483647;
                            else if (argomenti[2].length() > 9) s_nuovi = 2147483647;
                            else s_nuovi = Integer.parseInt(argomenti[2]);
                            economia.aggiornaHash(uuid, s_nuovi);
                            @SuppressWarnings("unchecked") List<String> mess = (List<String>) MyPlugin.getInstance().getConfig().getList("Economia.messaggioModificaSoldi");
                            assert mess != null;
                            sender.sendMessage(mess.get(0).replace("%money", String.valueOf(s_nuovi)).replaceAll("&", "§").replace("%user", target.getName()));
                            target.sendMessage(mess.get(1).replace("%money", economia.getSoldi(uuid).toString()).replaceAll("&", "§"));
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§4Formato soldi non valido");
                        }
                    } else {
                        sender.sendMessage("§4Nome player non valido o offline");
                        sender.sendMessage("§4Uso: /soldi set <nome_player> <soldi>");
                    }
                } else {
                    sender.sendMessage("§4Non possiedi i privilegi giusti");
                }
            } else {
                sender.sendMessage("§4/soldi");
                sender.sendMessage("§4/soldi set <nome_player> <soldi>");
            }
            return true;
        }



        return false;
    }

}


