package kiinse.plugin.coldysteps.initialize;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.utilities.placeholders.IndicatorExpansion;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class LoadAPI {
    public LoadAPI(@NotNull ColdySteps coldySteps) {
        coldySteps.sendLog("Registering PlaceHolderAPI...");
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new IndicatorExpansion(coldySteps).register();
            coldySteps.sendLog("PlaceHolderAPI registered");
            return;
        }
        coldySteps.sendLog(Level.INFO, "PlaceHolderAPI not found");
    }
}
