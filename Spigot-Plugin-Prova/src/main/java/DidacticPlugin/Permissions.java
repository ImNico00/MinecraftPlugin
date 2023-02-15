package DidacticPlugin;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;

public class Permissions {
    private static final HashMap<String, PermissionAttachment> perms = new HashMap<>();

    /*private static HashMap<String, PermissionAttachment> initializer() {
        HashMap<String, PermissionAttachment> hash = new HashMap();
        for (Player p : Bukkit.getOnlinePlayers()) {
            PermissionAttachment attachment = p.addAttachment(MyPlugin.plugin);
            hash.put(p.getUniqueId().toString(), attachment);
        }
        return hash;
    }*/

    public void setPermissions(Player p, String perm, boolean b) {
        String uuid = p.getUniqueId().toString();
        if (perms.containsKey(uuid)) {
            PermissionAttachment permesso = perms.get(uuid);
            permesso.setPermission(perm, b);
        } else {
            PermissionAttachment attachment = p.addAttachment(MyPlugin.plugin);
            perms.put(uuid, attachment);
            PermissionAttachment permesso = perms.get(uuid);
            permesso.setPermission(perm, b);
        }
    }
}
