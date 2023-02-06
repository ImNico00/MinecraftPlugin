import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
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
            s.setMotd(lista.next().replaceAll("&","§"));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String entrato = MyPlugin.getInstance().getConfig().getString("Settaggi.messaggioEntrata");
        Player p = e.getPlayer();
        String id = p.getUniqueId().toString();
        Economy eco = new Economy();

        if (entrato == null) entrato = "";
        e.setJoinMessage(entrato.replace("%user",e.getPlayer().getName()).replaceAll("&","§"));

        if (!p.hasPlayedBefore()) {
            eco.aggiornaHash(id,0);
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        String uscito = MyPlugin.getInstance().getConfig().getString("Settaggi.messaggioUscita");
        if (uscito == null) uscito = "";
        e.setQuitMessage(uscito.replace("%user",e.getPlayer().getName()).replaceAll("&","§"));
    }

}
