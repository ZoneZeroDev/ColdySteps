package kiinse.plugin.coldysteps.utilities.schedulers;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.biomes.interfaces.Biomes;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.data.effects.EffectsImpl;
import kiinse.plugin.coldysteps.data.worlds.interfaces.Worlds;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.utilities.permissions.PermissionUtils;
import kiinse.plugin.coldysteps.utilities.ColdUtils;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.api.schedulers.Scheduler;
import kiinse.plugins.darkwaterapi.api.schedulers.SchedulerData;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@SchedulerData(
        name = "ColdSchedule"
)
public class ColdSchedule extends Scheduler {

    private final ColdUtils coldUtils = new ColdUtils();
    private final PermissionUtils permissions;
    private final YamlFile config;
    private final List<String> effects;
    private final ColdySteps coldySteps;
    private final Worlds world;
    private final Biomes biomes;
    private final PlayerCold cold;

    public ColdSchedule(@NotNull ColdySteps coldySteps) {
        super(coldySteps);
        this.coldySteps = coldySteps;
        this.permissions = new PermissionUtils(coldySteps);
        this.config = coldySteps.getConfiguration();
        this.effects = new EffectsImpl(coldySteps).getEffects();
        this.cold = coldySteps.getCold();
        this.world = coldySteps.getWorlds();
        this.biomes = coldySteps.getBiomes();
    }

    @Override
    public void run() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (DarkPlayerUtils.isSurvivalAdventure(player)) {
                var allowedWorld = world.isPlayerInAllowedWorld(player);
                addEffects(allowedWorld, player);
                coldFunc(allowedWorld, player);
            }
        }
    }

    private void coldFunc(boolean allowedWorld, @NotNull Player player) {
        if (biomes.hasBiome(player) && allowedWorld) cold.addToPlayer(player, permissions.addCold(player));
        cold.removeFromPlayer(player, coldUtils.getColdRemoving(player));
        coldUtils.removeCold(player);
    }

    private void addEffects(boolean allowedWorld, @NotNull Player player) {
        if (((int) cold.getPlayerValue(player)) >= config.getInt(Config.ADD_EFFECTS_COLD) && config.getBoolean(Config.ADD_EFFECTS_ENABLE) && allowedWorld) {
            player.setFreezeTicks(100);
            for (var effect : effects) {
                try {
                    player.addPotionEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(effect)), 50, 1, false, false));
                } catch (Exception e) {
                    coldySteps.sendLog(Level.WARNING, "Error on giving player '" + DarkPlayerUtils.getPlayerName(player) + "' effect: " + e.getMessage());
                }
            }
        }
    }
}
