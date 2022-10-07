package kiinse.plugin.coldysteps.data.blocks.interfaces;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("unused")
public interface Blocks {

    void load() throws IllegalArgumentException;

    @NotNull HashMap<String, Double> getBlocksMap();

    boolean hasMaterial(@NotNull Material material);

    boolean hasBlock(@NotNull Block block);

    @NotNull String getMaterial(@NotNull Block block);

    boolean hasMaterial(@NotNull String material);

    double getValue(@NotNull Block block);

    double getValue(@NotNull Material material);

    boolean isBurningBlocksContains(@NotNull String stringValue);
}
