package jack.net.presenthunt.handler;

import jack.net.presenthunt.PresentHunt;
import jack.net.presenthunt.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


import java.util.List;
import java.util.Objects;

public class Reward {

    private final PresentHunt presentHunt;
    private final NamespacedKey key;
    private final ItemStack itemReward;
    private final List<String> commands;


    public Reward(PresentHunt presentHunt, ItemStack rewardItem, List<String> commands) {
        this.presentHunt = presentHunt;
        this.key = new NamespacedKey(presentHunt, "fallingblock");
        this.itemReward = rewardItem;
        this.commands = commands;
    }

    public void giveReward(Player player) {
        Location location = player.getLocation();
        for (int i = 0; i < 2; i++) {
            if (!itemReward.hasItemMeta()) return;
            ItemMeta meta = itemReward.getItemMeta();
            if (!meta.getDisplayName().equalsIgnoreCase("DIRT")) {
                location.getWorld().dropItemNaturally(location, itemReward);
                continue;
            }
            for (String c : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replace("%player%", player.getName()));
            }
        }
    }


    public void spawnOnPlayer(Player player) {
        Location location;
        location = player.getLocation().add(0, 70, 0);
        String falb = this.presentHunt.getConfig().getString("Falling-Block");
        if (falb == null) {
            System.out.println("Error");
        }
        Entity entity = (location.getWorld()).spawnFallingBlock(location, Material.matchMaterial(falb).createBlockData());
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, "fallingblock");
        player.sendMessage(CC.translate("&cYou have &fsummoned &ca present!"));
    }
}
