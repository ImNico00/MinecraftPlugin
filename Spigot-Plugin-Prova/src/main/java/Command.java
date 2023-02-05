import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String nomeComando, String[] argomenti) {

        if (command.getName().equalsIgnoreCase("MyCommand")) {
            if (sender instanceof Player p) {
                p.sendMessage(p.getName() + " sei un coglione...");
            } else {
                sender.sendMessage("§4Devi essere un player lol");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("Soldi")) {
            if (sender instanceof Player p) {
                Economy economia = new Economy();
                String uuid = p.getUniqueId().toString();
                if (argomenti.length != 0) {
                    try {
                        int s_nuovi;
                        if (argomenti[0].compareTo("2147483647") > 0) s_nuovi = 2147483647;
                        else if (argomenti[0].length() > 11 && argomenti[0].startsWith("-")) s_nuovi = -2147483647;
                        else s_nuovi = Integer.parseInt(argomenti[0]);
                        economia.aggiornaHash(uuid, s_nuovi);
                        p.sendMessage("Ora hai: " + s_nuovi);
                    } catch (NumberFormatException e) {
                        p.sendMessage("Devi inserire un numero coglione...");
                    }
                } else {
                    p.sendMessage("Hai " + economia.getSoldi(uuid).toString());
                }
            } else {
                sender.sendMessage("§4Devi essere un player lol");
            }
            return true;
        }

        return false;
    }
}
