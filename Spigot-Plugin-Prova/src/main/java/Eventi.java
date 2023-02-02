import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import java.util.Iterator;
import java.util.List;

public class Eventi implements Listener {
    static List<String> Motd = (List<String>) MyPlugin.getInstance().getConfig().getList("settings.motd");
    static Iterator<String> lista = Motd.iterator();
    @EventHandler
    public void onPing(ServerListPingEvent s) {
        if (lista.hasNext())
            s.setMotd(lista.next().replaceAll("&","\u00A7"));
        else {
            lista = Motd.iterator();
            s.setMotd(lista.next().replaceAll("&","\u00A7"));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        String entrato = MyPlugin.getInstance().getConfig().getString("settings.messaggioEntrata").replaceAll("&","§");
        if (entrato == null)
            e.setJoinMessage("");
        else
            e.setJoinMessage(entrato.replace("%user",e.getPlayer().getName()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        String uscito = MyPlugin.getInstance().getConfig().getString("settings.messaggioUscita").replaceAll("&","§");
        if (uscito == null)
            e.setQuitMessage("");
        else
            e.setQuitMessage(uscito.replace("%user",e.getPlayer().getName()));
    }

}
