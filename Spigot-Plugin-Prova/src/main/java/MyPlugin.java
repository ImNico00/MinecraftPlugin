import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {

    public static MyPlugin plugin;
    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage("§4Plugin DidacticPlugin disabilitato");
    }

    @Override
    public void onEnable(){
        super.onEnable();
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("§aPlugin DidacticPlugin caricato correttamente");

        //REGISTRAZIONE EVENTI
        Bukkit.getPluginManager().registerEvents(new Eventi(), this);

        //REGISTRAZIONE COMANDI
        getCommand("MyCommand").setExecutor(new Command());

        //CONFIGURAZIONI
        saveDefaultConfig();
    }

    public static MyPlugin getInstance() {
        return plugin;
    }

}
