package DidacticPlugin.BodyEvents;

import DidacticPlugin.MyPlugin;
import DidacticPlugin.Permissions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DeathListener implements Listener {

    private final MyPlugin plugin;
    public DeathListener(MyPlugin bodied) {
        this.plugin = bodied;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Location loc = p.getLocation();
        p.setLastDeathLocation(loc);
        Random rand = new Random();
        String random = String.valueOf(rand.nextInt(3000));
        Permissions permessi = MyPlugin.get_permissions();
        permessi.setPermissions(p, random, true);
        String death = e.getDeathMessage();
        assert death != null;
        e.setDeathMessage("");

        TextComponent mainText = new TextComponent("§c"+death);
        mainText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cRitorna dove sei morto").create()));
        mainText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/MyCommand tp_serializing_for_player " + random));
        p.spigot().sendMessage(mainText);

        for (Player t: Bukkit.getServer().getOnlinePlayers()) {
            if (t != p)
                t.sendMessage(death);
        }

        plugin.getBodyManager().getBodies().add(spawnBody(p));

        e.getDrops().clear();
    }

    @EventHandler
    public void onPlayerRightClick(@NotNull PlayerInteractAtEntityEvent e) {

        if (e.getRightClicked() instanceof ArmorStand armorStand) {
            Player p_who_clicked = e.getPlayer();
            Iterator<Body> bodyIterator = plugin.getBodyManager().getBodies().iterator();
            while (bodyIterator.hasNext()) {
                Body body = bodyIterator.next();
                if (body.getArmorStandList().contains(armorStand.getUniqueId())) {

                    p_who_clicked.playSound(p_who_clicked.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0f, 1.1f);

                    Location bodyLoc = body.getNpc().getBukkitEntity().getLocation();
                        for (ItemStack item : body.getItems()) {
                            if (item != null) {
                                p_who_clicked.getWorld().dropItem(bodyLoc, item);
                            }
                        }

                    plugin.getBodyManager().removeBodyNPC(body);

                    bodyIterator.remove();

                }

            }
        }

    }

    private Body spawnBody(Player p) {
        PlayerInventory p_inv = p.getInventory();
        ItemStack[] stack = Arrays.stream(p_inv.getContents()).filter(Objects::nonNull).toArray(ItemStack[]::new);
        Location location = p.getLastDeathLocation();
        assert location != null;



        CraftPlayer craftPlayer = (CraftPlayer) p;
        MinecraftServer server = craftPlayer.getHandle().getServer();
        ServerLevel level = craftPlayer.getHandle().getLevel();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), " ");

        GameProfile deadProfile = craftPlayer.getHandle().getGameProfile();
        Property prop = (Property) deadProfile.getProperties().get("textures").toArray()[0];
        String signature = prop.getSignature();
        String texture = prop.getValue();

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        assert server != null;
        ServerPlayer npc = new ServerPlayer(server, level, gameProfile, null);


        Location loc = location.clone();
        while (loc.getBlock().getType().isAir()) {
            loc.subtract(0, 1, 0);
        }

        npc.setPos(location.getX(), loc.getY()+1, location.getZ());
        npc.setPose(Pose.SLEEPING);
        List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>();
        equipmentList.add(new Pair<>(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(p_inv.getBoots())));
        equipmentList.add(new Pair<>(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(p_inv.getLeggings())));
        equipmentList.add(new Pair<>(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(p_inv.getChestplate())));
        equipmentList.add(new Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(p_inv.getHelmet())));


        Location armorLoc = location.clone();
        ArmorStand armorStand1 = (ArmorStand) p.getWorld().spawnEntity(armorLoc, EntityType.ARMOR_STAND);
        armorStand1.setSmall(true);
        armorStand1.setInvisible(true);
        armorStand1.setInvulnerable(true);
        armorStand1.setGravity(false);
        ArmorStand armorStand2 = (ArmorStand) p.getWorld().spawnEntity(armorLoc.subtract(1, 0, 0), EntityType.ARMOR_STAND);
        armorStand2.setSmall(true);
        armorStand2.setInvisible(true);
        armorStand2.setInvulnerable(true);
        armorStand2.setGravity(false);
        ArmorStand armorStand3 = (ArmorStand) p.getWorld().spawnEntity(armorLoc.subtract(1, 0, 0), EntityType.ARMOR_STAND);
        armorStand3.setSmall(true);
        armorStand3.setInvisible(true);
        armorStand3.setInvulnerable(true);
        armorStand3.setGravity(false);




        PlayerTeam team = new PlayerTeam(new Scoreboard(), npc.getName().getString());
        team.getPlayers().add(npc.getName().getString());
        team.setNameTagVisibility(Team.Visibility.NEVER);

        Bukkit.getOnlinePlayers().forEach(player -> {
            ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;
            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
            ps.send(new ClientboundAddPlayerPacket(npc));
            ps.send(new ClientboundSetEntityDataPacket(npc.getId(), npc.getEntityData(), true));
            ps.send(new ClientboundSetEquipmentPacket(npc.getId(), equipmentList));

            //remove the team
            ps.send(ClientboundSetPlayerTeamPacket.createRemovePacket(team));
            //add the team
            ps.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));

            new BukkitRunnable() {
                @Override
                public void run() {
                    ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));

                }
            }.runTaskLaterAsynchronously(plugin, 20L);
        });

        List<UUID> armorStands = new ArrayList<>();
        armorStands.add(armorStand1.getUniqueId());
        armorStands.add(armorStand2.getUniqueId());
        armorStands.add(armorStand3.getUniqueId());

        return new Body(p.getUniqueId(), npc, stack, armorStands, System.currentTimeMillis());
    }
}
