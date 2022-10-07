package kiinse.plugin.coldysteps.utilities.schedulers;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugins.darkwaterapi.api.schedulers.Scheduler;
import kiinse.plugins.darkwaterapi.api.schedulers.SchedulerData;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SchedulerData(
        name = "ColdyDamageSchedule"
)
public class DamageSchedule extends Scheduler {

    private final ColdySteps coldySteps;
    private final PlayerCold cold;

    public DamageSchedule(@NotNull ColdySteps coldySteps) {
        super(coldySteps);
        this.coldySteps = coldySteps;
        this.cold = coldySteps.getCold();
    }
    @Override
    public void run() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (DarkPlayerUtils.isSurvivalAdventure(player)) damagePlayer(player);
        }
    }

    private void damagePlayer(@NotNull Player player) {
        if (cold.getPlayerValue(player) >= 100 && player.getHealth() > 0.0)
            player.damage(coldySteps.getConfiguration().getInt(Config.DAMAGE));
    }
}
