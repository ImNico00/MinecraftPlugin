import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MyPlugin extends JavaPlugin {

    public static MyPlugin plugin;
    @Override
    public void onDisable() {
        saveMoney();
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage("[DidacticPlugin] §4Plugin disabilitato");
    }

    @Override
    public void onEnable(){
        super.onEnable();
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("[DidacticPlugin] §aPlugin caricato correttamente");

        //REGISTRAZIONE EVENTI
        Bukkit.getPluginManager().registerEvents(new Eventi(), this);

        //REGISTRAZIONE COMANDI
        PluginCommand command = getCommand("MyCommand");
        if (command != null) command.setExecutor(new Command());
        command = getCommand("Soldi");
        if (command != null) command.setExecutor(new Command());

        //CONFIGURAZIONI
        saveDefaultConfig();

        //CREAZIONE CARTELLE
        try {
            CreateFolders();
        } catch (IOException err) {
            System.out.println("Errore creazione cartelle: " + err);
        }
    }

    public static MyPlugin getInstance() {
        return plugin;
    }

    private static void CreateFolders() throws IOException {
        File ecdir = new File("economy/");
        File out = new File("economy/output.json");
        if (!ecdir.exists()) {
            if (ecdir.mkdirs()) throw new IOException();
            if (out.createNewFile()) throw new IOException();
        } else {
            if (!out.exists()) {
                if (out.createNewFile()) throw new IOException();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void saveMoney() {
        JSONObject jsonObject = new JSONObject();
        HashMap<String, Integer> hash = Economy.getHash();
        for (String p: hash.keySet()){
            jsonObject.put(p, hash.get(p));
        }
        FileWriter file;
        try {
            file = new FileWriter("economy/output.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
