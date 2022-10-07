package kiinse.plugin.coldysteps.data.biomes;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.biomes.interfaces.Biomes;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.enums.File;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BiomesImpl extends YamlFile implements Biomes {

    private final ColdySteps coldySteps;
    private final List<Biome> biomes = new ArrayList<>();

    public BiomesImpl(ColdySteps coldySteps) {
        super(coldySteps, File.BIOMES_YML);
        this.coldySteps = coldySteps;
        load();
    }

    @Override
    public void load() throws IllegalArgumentException {
        biomes.clear();
        var enabledBiomesList = getStringList(Config.BIOMES);
        if (!enabledBiomesList.isEmpty()) {
            for (var rawData : enabledBiomesList) {
                biomes.add(Biome.valueOf(rawData));
                coldySteps.sendLog(Level.CONFIG, "Biome: " + rawData);
            }
            coldySteps.sendLog("Biomes list loaded");
        } else {
            throw new IllegalArgumentException("Biomes list is empty!");
        }
    }

    @Override
    public @NotNull List<Biome> getBiomesList() {
        return new ArrayList<>(biomes);
    }

    @Override
    public boolean hasBiome(@NotNull Biome biome) {
        return biomes.contains(biome);
    }

    @Override
    public boolean hasBiome(@NotNull Player player) {
        return biomes.contains(player.getWorld().getBiome(player.getLocation()));
    }

    @Override
    public boolean isPlayerInAllowedColdBiome(@NotNull Player player) {
        return coldySteps.getWorlds().isPlayerInAllowedWorld(player) && hasBiome(player);
    }
}
