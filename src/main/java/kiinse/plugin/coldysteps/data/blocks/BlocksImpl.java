package kiinse.plugin.coldysteps.data.blocks;

import kiinse.plugin.coldysteps.data.blocks.interfaces.Blocks;
import kiinse.plugin.coldysteps.enums.BurningBlocks;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.enums.File;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class BlocksImpl extends YamlFile implements Blocks {

    private final DarkWaterJavaPlugin plugin;
    private final HashMap<String, Double> blocks = new HashMap<>();

    public BlocksImpl(@NotNull DarkWaterJavaPlugin plugin) {
        super(plugin, File.BLOCKS_YML);
        this.plugin = plugin;
        load();
    }

    @Override
    public void load() throws IllegalArgumentException {
        blocks.clear();
        var blocksList = getStringList(Config.BLOCKS);
        if (!blocksList.isEmpty()) {
            for (var rawData : blocksList) {
                try {
                    var raw = rawData.split(":");
                    blocks.put(raw[0], Double.valueOf(raw[1]));
                    plugin.sendLog(Level.CONFIG, "Block: " + raw[0] + " | Value: " + raw[1]);
                } catch (Exception e) {
                    plugin.sendLog(Level.WARNING, "Error on loading line '" + rawData + "' check it please.");
                }
            }
            plugin.sendLog("Blocks hashmap loaded");
        } else {
            throw new IllegalArgumentException("Blocks list is empty!");
        }
    }

    @Override
    public @NotNull HashMap<String, Double> getBlocksMap() {
        return new HashMap<>(blocks);
    }

    @Override
    public boolean hasMaterial(@NotNull Material material) {
        return hasMaterial(material.toString());
    }

    @Override
    public boolean hasBlock(@NotNull Block block) {
        return hasMaterial(getMaterial(block));
    }

    @Override
    public @NotNull String getMaterial(@NotNull Block block) {
        var material = block.getType().toString();
        if (isBurningBlocksContains(material) && ((Lightable) block.getBlockData()).isLit()) return "BURNING_" + material;
        return block.getType().toString();
    }

    @Override
    public boolean hasMaterial(@NotNull String material) {
        return blocks.containsKey(material);
    }

    @Override
    public double getValue(@NotNull Block block) {
        if (hasBlock(block)) return blocks.get(getMaterial(block));
        return 0.0;
    }

    @Override
    public double getValue(@NotNull Material material) {
        if (hasMaterial(material)) return blocks.get(material.toString());
        return 0.0;
    }

    @Override
    public boolean isBurningBlocksContains(@NotNull String stringValue) {
        for (var value : BurningBlocks.values()) {
            if (Objects.equals(stringValue, value.toString())) return true;
        }
        return false;
    }
}
