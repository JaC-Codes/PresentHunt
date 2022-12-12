package jack.net.presenthunt.itembuild;

import jack.net.presenthunt.PresentHunt;
import jack.net.presenthunt.handler.PresentHandler;
import jack.net.presenthunt.handler.Reward;
import jack.net.presenthunt.utils.CC;
import jack.net.presenthunt.utils.ItemBuild;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class PresentFlare implements Listener {

    private final PresentHunt presentHunt;
    private final NamespacedKey key;
    private final PresentHandler presentHandler;
    private final Reward reward;
    private ItemStack itemReward;
    private List<String> commands;

    public PresentFlare(PresentHunt presentHunt) {
        this.presentHunt = presentHunt;
        this.key = new NamespacedKey(presentHunt, "flare");
        this.presentHandler = new PresentHandler(presentHunt);
        this.reward = new Reward(presentHunt, itemReward, commands);
    }


    public ItemStack flare() {
        ItemStack flare = new ItemBuild(Material.REDSTONE_TORCH, 1).setDisplayName(CC.translate("&f&l** &r&c&lPresent Flare &f&l**")).setItemGlowing()
                .setUnsafeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setLore(CC.translate(" "),CC.translate(" &f&lRight click to summon a Present!")).build();

        ItemMeta meta = flare.getItemMeta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "flare");
        flare.setItemMeta(meta);
        return flare;
    }

    public void giveFlare(Player player, int amount) {
        ItemStack item = flare();
        item.setAmount(amount);
        player.getInventory().addItem(item);
    }

    @EventHandler
    public void flareRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (meta == null) return;
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    if (item.getAmount() >= 1) {
                        item.setAmount(item.getAmount() - 1);
                    }
                    reward.spawnOnPlayer(player);
                }
            }
        }
    }
}
