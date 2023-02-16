package DidacticPlugin;

import DidacticPlugin.BodyEvents.BodyManager;
import DidacticPlugin.BodyEvents.DeathListener;
import DidacticPlugin.Tasks.BodyRemoverTask;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MyPlugin extends JavaPlugin {

    public static MyPlugin plugin;
    @SuppressWarnings("unused")
    public static int version = getIntVersion()[0];
    @SuppressWarnings("unused")
    public static int release = getIntVersion()[1];
    public static Permissions perms = new Permissions();
    private BodyManager bodyManager;


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
        if (command != null) {
            command.setExecutor(new Command());
            command.setTabCompleter(new TabCompletion());
        }

        this.bodyManager = new BodyManager();
        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);
        BodyRemoverTask bodyRemover = new BodyRemoverTask(this, bodyManager);
        bodyRemover.runTaskTimerAsynchronously(this, 20L, 20L);

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

    @Override
    public void onDisable() {
        try {
            saveMoney();
        } catch (Exception e) {
            System.out.println("Errore salvataggio soldi");
        }

        super.onDisable();
        Bukkit.getConsoleSender().sendMessage("[DidacticPlugin] §4Plugin disabilitato");
    }

    private void saveMoney() {
        JsonObject object = new JsonObject();
        HashMap<String, Integer> hash = EconomiaSign.getHash();
        for (String p: hash.keySet()){
            object.addProperty(p, hash.get(p));
        }
        FileWriter file;
        try {
            file = new FileWriter("economy/output.json");
            file.write(object.toString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] getIntVersion() {
        int version = Integer.parseInt(Bukkit.getServer().getClass().getName().split("\\.")[3].split("_")[1]);
        int release = Integer.parseInt(Bukkit.getServer().getClass().getName().split("\\.")[3].split("R")[1]);
        return new int[]{version, release};
    }

    public static Permissions get_permissions() {
        return perms;
    }

    public BodyManager getBodyManager(){
        return bodyManager;
    }


}
