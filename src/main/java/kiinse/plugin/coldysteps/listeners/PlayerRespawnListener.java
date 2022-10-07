package kiinse.plugin.coldysteps.listeners;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class PlayerRespawnListener implements Listener {

    private final ColdySteps coldySteps = ColdySteps.getInstance();
    private final PlayerCold cold = coldySteps.getCold();

    @EventHandler
    public void respawnEvent(@NotNull PlayerRespawnEvent event){
        var player = event.getPlayer();
        cold.restorePlayer(player);
        coldySteps.sendLog(Level.CONFIG, "Player " + DarkPlayerUtils.getPlayerName(event.getPlayer()) + " died. His cold now is " + cold.getPlayerValue(player));
    }
}
