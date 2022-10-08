package kiinse.plugin.coldysteps;

import kiinse.plugin.coldysteps.data.biomes.BiomesImpl;
import kiinse.plugin.coldysteps.data.biomes.interfaces.Biomes;
import kiinse.plugin.coldysteps.data.blocks.BlocksImpl;
import kiinse.plugin.coldysteps.data.blocks.interfaces.Blocks;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.data.cold.json.PlayerColdJson;
import kiinse.plugin.coldysteps.data.cold.sql.ColdSQL;
import kiinse.plugin.coldysteps.data.cold.sql.PlayerColdSql;
import kiinse.plugin.coldysteps.data.food.FoodImpl;
import kiinse.plugin.coldysteps.data.food.interfaces.Food;
import kiinse.plugin.coldysteps.data.worlds.WorldsImpl;
import kiinse.plugin.coldysteps.data.worlds.interfaces.Worlds;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.initialize.*;
import kiinse.plugin.coldysteps.utilities.schedulers.ColdSchedule;
import kiinse.plugin.coldysteps.utilities.schedulers.DamageSchedule;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.exceptions.VersioningException;
import kiinse.plugins.darkwaterapi.api.files.locale.PlayerLocales;
import kiinse.plugins.darkwaterapi.api.indicators.Indicator;
import kiinse.plugins.darkwaterapi.api.indicators.IndicatorManager;
import kiinse.plugins.darkwaterapi.api.schedulers.SchedulersManager;
import kiinse.plugins.darkwaterapi.api.utilities.TaskType;
import kiinse.plugins.darkwaterapi.core.utilities.DarkUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkVersionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

public final class ColdySteps extends DarkWaterJavaPlugin {

    private Indicator indicator;
    private static ColdySteps instance;
    private PlayerLocales locales;
    private SchedulersManager schedulersManager;
    private IndicatorManager indicatorManager;
    private DamageSchedule damageSchedule;
    private ColdSchedule coldSchedule;
    private ColdSQL coldSQL;
    private PlayerCold cold;
    private Worlds worlds;
    private Biomes biomes;
    private Blocks blocks;
    private Food food;

    @Override
    public void onStart() throws Exception {
        instance = this;
        var darkWater = getDarkWaterAPI();
        indicatorManager = darkWater.getIndicatorManager();
        schedulersManager = darkWater.getSchedulersManager();
        locales = darkWater.getPlayerLocales();
        if (getConfiguration().getBoolean(Config.PG_ENABLED)) {
            coldSQL = new ColdSQL(this);
            cold = new PlayerColdSql(this);
        } else {cold = new PlayerColdJson(this);}
        worlds = new WorldsImpl(this);
        biomes = new BiomesImpl(this);
        blocks = new BlocksImpl(this);
        food = new FoodImpl(this);
        new LoadAPI(this);
        new RegisterEvents(this);
        indicator = Indicator.valueOf(this, "%coldy_indicator%", 2);
        damageSchedule = new DamageSchedule(this);
        coldSchedule = new ColdSchedule(this);
        if (getConfiguration().getBoolean(Config.INDICATOR_ACTIONBAR))
            indicatorManager.register(this, indicator);
        schedulersManager.register(damageSchedule);
        schedulersManager.register(coldSchedule);
        checkForUpdates();
    }

    @Override
    public void onStop() throws Exception {
        cold.save();
        indicatorManager.removeIndicator(indicator);
        schedulersManager.unregister(damageSchedule);
        schedulersManager.unregister(coldSchedule);

    }

    private void checkForUpdates() {
        DarkUtils.runTask(TaskType.ASYNC, this, () -> {
            if (!getConfiguration().getBoolean(Config.DISABLE_VERSION_CHECK)) {
                try {
                    var latest = DarkVersionUtils.getLatestGithubVersion("https://github.com/NubilumDev/ColdySteps");
                    if (!latest.isGreaterThan(DarkVersionUtils.getPluginVersion(this))) {
                        sendLog("Latest version of ColdySteps installed, no new versions found <3");
                        return;
                    }
                    var reader = new BufferedReader(new InputStreamReader(
                            Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("version-message.txt"))));
                    var builder = new StringBuilder("\n");
                    while (reader.ready())
                        builder.append(reader.readLine()).append("\n");
                    sendConsole(DarkUtils.replaceWord(builder.toString(), new String[]{
                            "{NEW_VERSION}:" + latest.getOriginalValue(),
                            "{CURRENT_VERSION}:" + getDescription().getVersion()
                    }));
                } catch (IOException | VersioningException e) {
                    sendLog(Level.WARNING, "Error while checking ColdySteps version! Message: " + e.getMessage());
                }
            }
        });
    }

    public @NotNull PlayerLocales getLocales() {
        return locales;
    }
    public @NotNull PlayerCold getCold() {return cold;}
    public @NotNull ColdSQL getColdSQL() {
        return coldSQL;
    }
    public @NotNull Worlds getWorlds() {
        return worlds;
    }
    public @NotNull Biomes getBiomes() {
        return biomes;
    }
    public @NotNull Blocks getBlocks() {
        return blocks;
    }
    public @NotNull Food getFood() {
        return food;
    }
    public static @NotNull ColdySteps getInstance() {
        return instance;
    }
}

