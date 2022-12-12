package jack.net.presenthunt;


import jack.net.presenthunt.commands.PlayerPresentSummon;
import jack.net.presenthunt.handler.PresentHandler;
import jack.net.presenthunt.commands.FlareCommand;
import jack.net.presenthunt.handler.RandomCollection;
import jack.net.presenthunt.handler.Reward;
import jack.net.presenthunt.itembuild.PresentFlare;
import jack.net.presenthunt.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;


public class PresentHunt extends JavaPlugin {

    private static PresentHunt instance;
    public static RandomCollection<Reward> rand = new RandomCollection<>();
    private static ItemStack item;
    private final PresentHandler presentHandler = new PresentHandler(this);

    public void onEnable() {
        this.loadRewards();
        presentHandler.phTimer();
        instance = this;
        this.Config();
        long duration = System.currentTimeMillis();
        String prefix = "§3[" + getDescription().getName() + " " + getDescription().getVersion() + "] ";
        Bukkit.getConsoleSender().sendMessage(prefix + "§6=== ENABLE START ===");
        Bukkit.getConsoleSender().sendMessage(prefix + "§aLoading §dListeners");
        registerEvents();
        Bukkit.getConsoleSender().sendMessage(prefix + "§aLoading §dCommands");
        registerCommands();
        Bukkit.getConsoleSender().sendMessage(prefix + "§aMade by §dJack");
        Bukkit.getConsoleSender().sendMessage(
                prefix + "§6=== ENABLE §aCOMPLETE §6(Took §d" + (System.currentTimeMillis() - duration) + "ms§6) ===");

    }

    public Reward getRand() {
        return rand.getRandom();
    }

    public void Config() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void registerCommands() {
        getCommand("ph").setExecutor(new FlareCommand(this));
        getCommand("phplayer").setExecutor(new PlayerPresentSummon(this));
    }

    private void registerEvents() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PresentHandler(this), this);
        manager.registerEvents(new PresentFlare(this), this);
    }

    public void loadRewards() {
        for (String i : this.getConfig().getConfigurationSection("Rewards").getKeys(false)) {
            String materialString = this.getConfig().getString("Rewards." + i + ".item");
            if (materialString == null) {
                getLogger().warning("** Material for reward " + i + "is missing! **");
                continue;
            }
            Material material = Material.matchMaterial(materialString);
            if (material == null) {
                getLogger().warning("** Material for reward " + i + "is missing! **");
                continue;
            }

            ItemStack item = new ItemStack(material);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(CC.translate(this.getConfig().getString("Rewards." + i + ".name")));
            List<String> lore = new ArrayList<>();
            meta.setLore(CC.translate(this.getConfig().getStringList("Rewards." + i + ".lore")));
            item.setAmount(this.getConfig().getInt("Rewards." + i + ".amount"));

            boolean enchantMe = this.getConfig().getBoolean("Rewards." + i + ".enable-enchants");
            List<String> commands = this.getConfig().getStringList("Rewards." + i + ".commands");

            if (enchantMe) {
                for (String enchant : this.getConfig().getStringList("Rewards." + i + ".enchants")) {
                    String enchantName = enchant.split(":")[0];
                    int enchantLevel = Integer.parseInt(enchant.split(":")[1]);
                    meta.addEnchant(Enchantment.getByName(enchantName), enchantLevel, true);
                }
            }


            Reward itemReward = new Reward(this, item, commands);

            item.setItemMeta(meta);
            rand.add(this.getConfig().getDouble("Rewards." + i + ".chance"), itemReward);
        }
    }

    public void onDisable() {
        long duration = System.currentTimeMillis();
        String prefix = "§3[" + getDescription().getName() + " " + getDescription().getVersion() + "] ";
        Bukkit.getConsoleSender().sendMessage(prefix + "§6=== DISABLING ===");
        Bukkit.getConsoleSender().sendMessage(prefix + "§aDisabling §dListeners");
        Bukkit.getConsoleSender().sendMessage(prefix + "§aDisabling §dCommands");
        Bukkit.getConsoleSender().sendMessage(prefix + "§aMade by §dJack");
        Bukkit.getConsoleSender().sendMessage(
                prefix + "§6=== DISABLE §aCOMPLETE §6(Took §d" + (System.currentTimeMillis() - duration) + "ms§6) =");
    }
}
