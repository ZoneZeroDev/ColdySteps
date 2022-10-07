package kiinse.plugin.coldysteps.data.biomes.interfaces;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public interface Biomes {

    void load() throws IllegalArgumentException;

    @NotNull List<Biome> getBiomesList();

    boolean hasBiome(@NotNull Biome biome);

    boolean hasBiome(@NotNull Player player);

    boolean isPlayerInAllowedColdBiome(@NotNull Player player);
}
