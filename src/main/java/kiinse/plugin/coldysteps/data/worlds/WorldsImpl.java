package kiinse.plugin.coldysteps.data.worlds;

import kiinse.plugin.coldysteps.data.worlds.interfaces.Worlds;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.enums.File;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WorldsImpl extends YamlFile implements Worlds {

    private final DarkWaterJavaPlugin plugin;
    private final List<String> worlds = new ArrayList<>();

    public WorldsImpl(@NotNull DarkWaterJavaPlugin plugin) {
        super(plugin, File.WORLDS_YML);
        this.plugin = plugin;
        load();
    }

    @Override
    public void load() throws IllegalArgumentException {
        worlds.clear();
        var worldsList = getStringList(Config.WORLDS);
        if (!worldsList.isEmpty()) {
            for (var rawData : worldsList) {
                worlds.add(rawData);
                plugin.sendLog(Level.CONFIG, "World: " + rawData);
            }
            plugin.sendLog("Worlds list loaded");
        } else {
            throw new IllegalArgumentException("Worlds list is empty!");
        }
    }

    @Override
    public @NotNull List<String> getWorldsList() {
        return new ArrayList<>(worlds);
    }

    @Override
    public boolean hasWorld(@NotNull World world) {
        return worlds.contains(world.getName());
    }

    @Override
    public boolean isPlayerInAllowedWorld(@NotNull Player player) {
        return hasWorld(player.getWorld());
    }
}
