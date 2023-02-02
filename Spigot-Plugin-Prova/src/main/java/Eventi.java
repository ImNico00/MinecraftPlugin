import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Eventi implements Listener {


    //e.getPlayer().getName()

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        String entrato = MyPlugin.getInstance().getConfig().getString("settings.messaggioEntrata");
        if (entrato == null)
            e.setJoinMessage("");
        else
            e.setJoinMessage(entrato.replace("%user",e.getPlayer().getName()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        String uscito = MyPlugin.getInstance().getConfig().getString("settings.messaggioUscita");
        if (uscito == null)
            e.setQuitMessage("");
        else
            e.setQuitMessage(uscito.replace("%user",e.getPlayer().getName()));
    }

}
