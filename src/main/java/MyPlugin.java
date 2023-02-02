import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MyPlugin extends JavaPlugin {

    public static MyPlugin plugin;
    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage("§4Plugin GERVASI disabilitato");
    }

    @Override
    public void onEnable(){
        super.onEnable();
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("§aPlugin GERVASI caricato correttamente");

        getCommand("gerv").setExecutor(new Command());

    }

    public static MyPlugin getInstance() {
        return plugin;
    }

}
