package kiinse.plugin.coldysteps.listeners;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.utilities.ColdUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

public class EatListener implements Listener {

    private final ColdUtils coldUtils = new ColdUtils();

    @EventHandler
    public void drinkEvent(@NotNull PlayerItemConsumeEvent event) {
        if (ColdySteps.getInstance().getConfiguration().getBoolean(Config.REMOVE_FOOD)) {
            var item = event.getItem();
            coldUtils.restoreByFood(event, item);
        }
    }
}
