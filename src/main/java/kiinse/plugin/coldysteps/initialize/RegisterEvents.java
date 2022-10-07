package kiinse.plugin.coldysteps.initialize;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.listeners.EatListener;
import kiinse.plugin.coldysteps.listeners.OnJoinListener;
import kiinse.plugin.coldysteps.listeners.PlayerDeathListener;
import kiinse.plugin.coldysteps.listeners.PlayerRespawnListener;
import org.jetbrains.annotations.NotNull;

public class RegisterEvents {

    public RegisterEvents(@NotNull ColdySteps coldySteps){
        coldySteps.sendLog("Registering listeners...");
        coldySteps.getServer().getPluginManager().registerEvents(new EatListener(), coldySteps);
        coldySteps.getServer().getPluginManager().registerEvents(new OnJoinListener(), coldySteps);
        coldySteps.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), coldySteps);
        coldySteps.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), coldySteps);
        coldySteps.sendLog("Listeners registered");
    }

}
