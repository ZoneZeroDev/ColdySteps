package kiinse.plugin.coldysteps.listeners;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.enums.Message;
import kiinse.plugin.coldysteps.enums.ColdyReplace;
import kiinse.plugins.darkwaterapi.api.files.messages.MessagesUtils;
import kiinse.plugins.darkwaterapi.core.files.messages.DarkMessagesUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {

    private final ColdySteps coldySteps = ColdySteps.getInstance();
    private final MessagesUtils messagesUtils = new DarkMessagesUtils(coldySteps);
    private final PlayerCold cold = coldySteps.getCold();

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        var player = event.getEntity();
        if (cold.getPlayerValue(player) >= 100.0) {
            event.setDeathMessage("");
            messagesUtils.sendMessageToAllWithReplace(Message.DEATH_MESSAGE, ColdyReplace.PLAYER, DarkPlayerUtils.getPlayerName(player));
        }
    }
}
