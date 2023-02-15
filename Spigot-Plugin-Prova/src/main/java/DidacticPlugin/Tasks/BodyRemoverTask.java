package DidacticPlugin.Tasks;

import DidacticPlugin.BodyEvents.Body;
import DidacticPlugin.BodyEvents.BodyManager;
import DidacticPlugin.MyPlugin;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class BodyRemoverTask extends BukkitRunnable {
    private final MyPlugin plugin;
    private final BodyManager bodyManager;

    public BodyRemoverTask(MyPlugin plugin, BodyManager bodyManager) {
        this.plugin = plugin;
        this.bodyManager = bodyManager;
    }

    @Override
    public void run() {

        Iterator<Body> bodyIterator = bodyManager.getBodies().iterator();
        while(bodyIterator.hasNext()) {
            Body body = bodyIterator.next();

            long now = System.currentTimeMillis();
            if ((now - body.getWhen_died()) >= (10000)) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        Location location = body.getNpc().getBukkitEntity().getLocation().clone();
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;
                            body.getNpc().setPos(location.getX(), location.getY() - 0.01, location.getZ());
                            ps.send(new ClientboundTeleportEntityPacket(body.getNpc()));
                        });

                        if(!location.add(0, 1, 0).getBlock().isPassable()) {
                            bodyManager.removeBodyNPC(body);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 5L);

                Player playerWhoDied = Bukkit.getPlayer(body.getWho_died());
                if (playerWhoDied != null) {
                    playerWhoDied.sendMessage("Non hai reclamato il tuo corpo entro 15 minuti perciò è stato rimosso");
                }
                bodyIterator.remove();
            }

        }

    }

}
