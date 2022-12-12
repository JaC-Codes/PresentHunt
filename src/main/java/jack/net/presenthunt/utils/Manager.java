package jack.net.presenthunt.utils;

import jack.net.presenthunt.PresentHunt;
import org.bukkit.entity.Player;

public class Manager {

    private final PresentHunt presentHunt;

    public Manager(PresentHunt presentHunt) {
        this.presentHunt = presentHunt;
    }

    public void usage(Player player) {
        player.sendMessage(CC.translate("&7Usage: &c/ph give <player> flare 1"));
    }
}
