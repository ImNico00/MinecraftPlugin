package DidacticPlugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Economy {

    private static final HashMap<String, Integer> hash = staticInitializer();


    private static HashMap<String, Integer> staticInitializer (){
        HashMap<String, Integer> main = new HashMap<>();
        JsonParser parser = new JsonParser();

        try {
            JsonObject obj = parser.parse(new FileReader("economy/output.json")).getAsJsonObject();
            for (Map.Entry<String, JsonElement> p: obj.entrySet()) {
                int s;
                String stringa = p.getValue().toString();
                try {
                    if (stringa.length() > 10 && stringa.startsWith("-")) s = -2147483647;
                    else if (stringa.length() > 9) s = 2147483647;
                    else s = Integer.parseInt(stringa);
                } catch(NumberFormatException e) {
                    s = 0;
                }
                main.put(p.getKey(), s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return main;
    }

    public void aggiornaHash(String uuid, Integer soldi) {
        hash.put(uuid, soldi);
    }

    public Integer getSoldi(String uuid) {
        return hash.get(uuid);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Integer> getHash() {
        return (HashMap<String, Integer>) hash.clone();
    }
}
