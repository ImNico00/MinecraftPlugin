package DidacticPlugin;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.NotNull;


import java.util.*;

public class Eventi implements Listener {
    @SuppressWarnings("unchecked") static List<String> Motd = MyPlugin.getInstance().getConfig().getList("Settaggi.motd") != null ? (List<String>) MyPlugin.getInstance().getConfig().getList("Settaggi.motd") : Arrays.asList("Hello", "World!");
    static Iterator<String> lista = staticInitializer();

    static private Iterator<String> staticInitializer() {
        Iterator<String> s;
        try {
            s = Motd.iterator();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
        return s;
    }

    @EventHandler
    public void onPing(ServerListPingEvent s) {
        if (lista.hasNext())
            s.setMotd(lista.next().replaceAll("&","§"));
        else {
            lista = Motd.iterator();
            s.setMotd(lista.next().replaceAll("&", "§"));
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        String entrato = MyPlugin.getInstance().getConfig().getString("Settaggi.messaggioEntrata");
        Player p = e.getPlayer();
        String id = p.getUniqueId().toString();
        EconomiaSign eco = new EconomiaSign();

        if (entrato == null) entrato = "";
        for (Player t: Bukkit.getServer().getOnlinePlayers()) {
            if (t != p)
                t.sendMessage(entrato.replace("%user",e.getPlayer().getName()).replaceAll("&","§"));
        }
        Bukkit.getConsoleSender().sendMessage(entrato.replace("%user",e.getPlayer().getName()).replaceAll("&","§"));
        e.setJoinMessage("");

        if (!EconomiaSign.getHash().containsKey(id)) {
            eco.aggiornaHash(id,0);
        }

        String join_mirato = "Settaggi.messaggioMirato_AltreJoin";

        if (!p.hasPlayedBefore()) {
            join_mirato = "Settaggi.messaggioMirato_PrimaJoin";
        }

        // INVIO LISTA MESSAGGI
        @SuppressWarnings("unchecked") List<String> joinMirato = (List<String>) MyPlugin.getInstance().getConfig().getList(join_mirato);

        if (joinMirato != null) {
            for (String m : joinMirato)
                e.getPlayer().sendMessage(m.replaceAll("&", "§"));
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        String uscito = MyPlugin.getInstance().getConfig().getString("Settaggi.messaggioUscita");
        if (uscito == null) uscito = "";
        e.setQuitMessage(uscito.replace("%user",e.getPlayer().getName()).replaceAll("&","§"));
    }

    @EventHandler
    public void onChat(@NotNull AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String change = MyPlugin.getInstance().getConfig().getString("Permessi.chat");
        String prefix = MyPlugin.getInstance().getConfig().getString("Permessi.prefissoChat");
        String format;
        assert change != null && prefix != null;
        change = change.replaceAll("&","§");
        prefix = prefix.replaceAll("&","§");

        if (p.hasPermission("didacticplugin.setsoldi") || p.hasPermission("didacticplugin.*")) {
            String newName = MyPlugin.getInstance().getConfig().getString("Permessi.nomeAdmin");
            assert newName != null;
            newName = newName.replaceAll("&","§").replace("%user",p.getName());
            p.setPlayerListName(newName);
            p.setDisplayName(newName);
        } else {
            p.setPlayerListName(p.getName());
            p.setDisplayName(p.getName());
        }
        format = prefix + p.getDisplayName() + change.replace("%msg",e.getMessage());

        if (p.hasPermission("didacticplugin.chatcolor") || p.hasPermission("didacticplugin.*")) {
            format = format.replaceAll("&","§");
        }

        e.setFormat(format);
    }

    @EventHandler
    public void CommandModifier(@NotNull PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String[] cmd = e.getMessage().split(" ");
        if (cmd[0].contains("tell") || cmd[0].contains("msg")) {
            String messaggio = e.getMessage().split(cmd[0] + " " + cmd[1])[1];
            e.setCancelled(true);
            Player target = Bukkit.getPlayer(cmd[1]);
            if (target == null) {
                p.sendMessage("§4Puoi inviare messaggi solo ad un altro player");
            } else {
                if (p == target) p.sendMessage("§cNon puoi inviarti messaggi da solo...");
                else {
                    target.sendMessage("§c" + p.getDisplayName() + " §7» §c" + target.getDisplayName() + "§7:" + messaggio);
                    p.sendMessage("§c" + p.getDisplayName() + " §7» §c" + target.getDisplayName() + "§7:" + messaggio);
                }
            }
        }

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void CommandModifier(@NotNull ServerCommandEvent e) {
        String[] cmd = e.getCommand().split(" ");
        if (cmd[0].contains("tell") || cmd[0].contains("msg")) {
            String messaggio = e.getCommand().split(cmd[0] + " " + cmd[1])[1];
            Player target = Bukkit.getPlayer(cmd[1]);
            e.setCancelled(true);
            if (target == null) {
                if (cmd[1].equalsIgnoreCase("@a")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        TextComponent mainText = new TextComponent("§c§nServer§7 » §cA TUTTI§7:" + messaggio);
                        mainText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cQuesto messaggio è stato inviato dal server").create()));
                        p.spigot().sendMessage(mainText);
                    }
                    e.getSender().sendMessage("§cServer §7-> §cA TUTTI§7:" + messaggio);
                } else {
                    e.getSender().sendMessage("§4Puoi inviare messaggi solo ad un altro player o usando @a");
                }
            } else {
                TextComponent mainText = new TextComponent("§c§nServer§7 » §c" + target.getDisplayName() + "§7:" + messaggio);
                mainText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cQuesto messaggio è stato inviato dal server").create()));
                target.spigot().sendMessage(mainText);
                e.getSender().sendMessage("§cServer §7-> §c" + target.getDisplayName() + "§7:" + messaggio);
            }
        }

    }


}


