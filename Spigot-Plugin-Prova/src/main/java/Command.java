import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String nomeComando, String[] argomenti) {

        if (command.getName().equalsIgnoreCase("MyCommand")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.sendMessage(p.getName() + " sei un coglione...");
            } else {
                sender.sendMessage("§4Devi essere un player lol");
            }
            return true;
        }

        return false;
    }
}
