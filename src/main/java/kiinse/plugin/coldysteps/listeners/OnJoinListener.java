package kiinse.plugin.coldysteps.listeners;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.utilities.ColdUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class OnJoinListener implements Listener {

    private final ColdySteps coldySteps = ColdySteps.getInstance();
    private final PlayerCold cold = coldySteps.getCold();
    private final ColdUtils coldUtils = new ColdUtils();

    @EventHandler
    public void joinEvent(@NotNull PlayerJoinEvent event) {
        var player = event.getPlayer();
        coldUtils.addColdPlayer(player);
        coldySteps.sendLog( Level.CONFIG, "Player " + DarkPlayerUtils.getPlayerName(player) + " joined. His cold is " + cold.getPlayerValue(player));
    }
}
