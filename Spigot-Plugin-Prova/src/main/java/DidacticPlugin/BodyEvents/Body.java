package DidacticPlugin.BodyEvents;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class Body {
    private final ServerPlayer npc;
    private final UUID who_died;
    private final long when_died;
    private final ItemStack[] items;
    private final List<UUID> armorStandList;


    public Body(UUID whoDied, ServerPlayer npc, ItemStack[] items, List<UUID> armorStands, long whenDied) {
        this.who_died = whoDied;
        this.npc = npc;
        this.items = items;
        this.armorStandList = armorStands;
        this.when_died = whenDied;
    }

    public List<UUID> getArmorStandList() {
        return armorStandList;
    }

    public ServerPlayer getNpc() {
        return npc;
    }


    public UUID getWho_died() {
        return who_died;
    }


    public long getWhen_died() {
        return when_died;
    }

    public ItemStack[] getItems() {
        return items;
    }


}
