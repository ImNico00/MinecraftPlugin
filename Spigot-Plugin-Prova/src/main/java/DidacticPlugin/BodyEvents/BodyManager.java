package DidacticPlugin.BodyEvents;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BodyManager {
    private final List<Body> bodies;

    public BodyManager() {
        this.bodies = new ArrayList<>();
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void removeBodyNPC(Body body) {
        //remove the npc for every player on the server
        Bukkit.getOnlinePlayers().forEach(player -> {
            ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;
            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, body.getNpc()));
            ps.send(new ClientboundRemoveEntitiesPacket(body.getNpc().getId()));
        });
        for (UUID armor : body.getArmorStandList()) {
            Entity armorStand = Bukkit.getServer().getEntity(armor);
            assert armorStand != null;
            armorStand.remove();
        }
    }

}
