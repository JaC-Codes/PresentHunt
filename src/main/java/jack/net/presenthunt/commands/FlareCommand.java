package jack.net.presenthunt.commands;

import jack.net.presenthunt.PresentHunt;
import jack.net.presenthunt.itembuild.PresentFlare;
import jack.net.presenthunt.handler.PresentHandler;
import jack.net.presenthunt.utils.CC;
import jack.net.presenthunt.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlareCommand implements CommandExecutor {

    private final PresentHunt presentHunt;
    private final PresentHandler presentHandler;
    private final PresentFlare presentFlare;
    private final Manager manager;

    public FlareCommand(PresentHunt presentHunt) {
        this.presentHunt = presentHunt;
        this.presentHandler = new PresentHandler(presentHunt);
        this.presentFlare = new PresentFlare(presentHunt);
        this.manager = new Manager(presentHunt);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if (!player.hasPermission("ph.admin")) {
            player.sendMessage("&7You do &cnot &7have permission to use this command.");
        }
        if (args.length < 4) {
            manager.usage(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                if (args[2].equalsIgnoreCase("flare")) {
                    int amount = Integer.parseInt(args[3]);
                    presentFlare.giveFlare(target, amount);
                    target.sendMessage(CC.translate("&7You received &f&l" + amount + " &cPresent Flares."));
                    player.sendMessage(CC.translate("&7You have given &f&l" + target.getDisplayName() + " &c" + amount + " &cPresent Flares."));
                    return true;
                }
            }
        }
        return false;
    }
}

