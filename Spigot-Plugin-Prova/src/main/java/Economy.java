import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Economy {

    private static final HashMap<String, Integer> hash = staticInitializer();

    private static HashMap<String, Integer> staticInitializer (){
        HashMap<String, Integer> main = new HashMap<>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("economy/output.json"));
            JSONObject jsonObject = (JSONObject)obj;
            for (Object p: jsonObject.keySet()) {
                int s;
                String stringa = jsonObject.get(p.toString()).toString();
                try {
                    if (stringa.compareTo("2147483647") > 0) s = 2147483647;
                    else if (stringa.length() > 11 && stringa.startsWith("-")) s = -2147483647;
                    else s = Integer.parseInt(stringa);
                } catch(NumberFormatException e) {
                    s = 0;
                }
                main.put(p.toString(), s);
            }
        } catch (IOException | ParseException e) {
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
