package jack.net.presenthunt.commands;

import jack.net.presenthunt.PresentHunt;
import jack.net.presenthunt.handler.PresentHandler;
import jack.net.presenthunt.handler.Reward;
import jack.net.presenthunt.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerPresentSummon implements CommandExecutor {

    private final PresentHunt presentHunt;
    private final PresentHandler presentHandler;
    private final Reward reward;
    private ItemStack rewardItem;
    private List<String> commands;

    public PlayerPresentSummon(PresentHunt presentHunt) {
        this.presentHunt = presentHunt;
        this.presentHandler = new PresentHandler(presentHunt);
        this.reward = new Reward(presentHunt, rewardItem, commands);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if (sender == null) {
            return false;
        } else{
            reward.spawnOnPlayer(player);
            sender.sendMessage(CC.translate("&7You have summoned a &cPresent Hunt&7."));
        }


        return false;
    }
}