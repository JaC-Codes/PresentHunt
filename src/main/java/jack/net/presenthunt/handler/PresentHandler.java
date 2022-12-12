package jack.net.presenthunt.handler;

import com.jeff_media.customblockdata.CustomBlockData;
import jack.net.presenthunt.PresentHunt;
import jack.net.presenthunt.utils.CC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class PresentHandler implements Listener {

    private final PresentHunt presentHunt;
    private final NamespacedKey key;
    private final NamespacedKey storedItemKey;
    private final Reward reward;
    private List<String> commands;
    private ItemStack rewardItem;
    private ArmorStand hologram;
    Random random = new Random();


    public PresentHandler(PresentHunt presentHunt) {
        this.presentHunt = presentHunt;
        this.key = new NamespacedKey(presentHunt, "fallingblock");
        this.storedItemKey = new NamespacedKey(presentHunt, "fallenchest");
        this.reward = new Reward(presentHunt, rewardItem, commands);
    }

    @EventHandler
    public void onBlockLand(EntityChangeBlockEvent event) {
        if (!(event.getEntityType() == EntityType.FALLING_BLOCK)) return;
        if (!event.getEntity().isOnGround()) return;
        if (event.getEntity().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(presentHunt, () -> {

                String landCrate = this.presentHunt.getConfig().getString("Landed-Crate");
                if (landCrate == null) {
                    System.out.println("** Can't find config path **");
                    return;
                }

                Material material = Material.matchMaterial(landCrate);
                if (material == null) {
                    System.out.println("** Cant match material **");
                    return;
                }

                Block block = event.getBlock();
                block.setType(material);
                PersistentDataContainer customBlockData = new CustomBlockData(event.getBlock(), presentHunt);
                customBlockData.set(storedItemKey, PersistentDataType.STRING, "Thisistheblock");
                if (block.getType().equals(material)) {
                    hologram = (ArmorStand) block.getWorld().spawnEntity(block.getLocation().add(0.5, -1, 0.5), EntityType.ARMOR_STAND);
                    hologram.setInvisible(true);
                    hologram.setCustomNameVisible(true);
                    hologram.setGravity(false);
                    hologram.setCustomName(CC.translate("&f&l** &c&lPresent Crate &f&l**"));
                }
            }, 1);

        }
    }

    @EventHandler
    public void chestBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!block.getType().equals(Material.CHEST)) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || (event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
            if (event.getClickedBlock() == null) return;
            final PersistentDataContainer customBlockData = new CustomBlockData(event.getClickedBlock(), presentHunt);
            if (customBlockData.has(storedItemKey, PersistentDataType.STRING)) {
                event.setCancelled(true);
                event.getClickedBlock().setType(Material.AIR);
                this.presentHunt.getRand().giveReward(player);
                hologram.remove();


            }

        }
    }

    public void getRandomLocation(World world) {
        Location location;
        world = Bukkit.getWorld("world");
        Location centerLoc = new Location(world, 0, 0, 0);
        Random random = new Random();
        double randomX = centerLoc.getX() - Math.round(random.nextDouble(5000)) + Math.round(random.nextDouble(5000));
        double randomZ = centerLoc.getY() - Math.round(random.nextDouble(5000)) + Math.round(random.nextDouble(5000));
        location = new Location(world, randomX, 0, randomZ);
        location.setY(world.getHighestBlockYAt(location) + 1);
        String falb = this.presentHunt.getConfig().getString("Falling-Block");
        if (falb == null) {
            System.out.println("Error");
        }
        Entity entity = (location.getWorld()).spawnFallingBlock(location, Material.matchMaterial(falb).createBlockData());
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, "fallingblock");
        Bukkit.broadcastMessage(CC.translate("""
                \s
                &c&lA Present has fallen at!
                &7Coordinates:\040""" +
                "&c&lx: &7" + (int) randomX + " &c&ly: &7" + (int) randomZ + "&r"));
    }

    public void phTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(presentHunt, new Runnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");
                getRandomLocation(world);
            }

        }, 30000);

    }
}


