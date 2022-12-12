package jack.net.presenthunt.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuild {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;


    public ItemBuild(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    private void updateItemMeta() {
        this.itemStack.setItemMeta(this.itemMeta);
    }

    public ItemBuild setDisplayName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuild setLore(String... lines) {
        this.itemMeta.setLore(Arrays.asList(lines));
        return this;
    }

    public ItemBuild setItemGlowing() {
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        this.updateItemMeta();
        return this.itemStack;
    }

    public ItemBuild setEnchant(Enchantment enchant, int level) {
        this.itemMeta.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuild setUnsafeEnchant(Enchantment enchant, int level) {
        this.itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }
}
