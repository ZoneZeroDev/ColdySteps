package kiinse.plugin.coldysteps.data.food;

import kiinse.plugin.coldysteps.data.food.interfaces.Food;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.enums.File;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.logging.Level;

public class FoodImpl extends YamlFile implements Food {

    private final DarkWaterJavaPlugin plugin;
    private final HashMap<Material, Double> food = new HashMap<>();

    public FoodImpl(@NotNull DarkWaterJavaPlugin plugin) {
        super(plugin, File.FOOD_YML);
        this.plugin = plugin;
        load();
    }

    @Override
    public void load() throws IllegalArgumentException {
        food.clear();
        var foodList = getStringList(Config.FOOD);
        if (!foodList.isEmpty()) {
            for (var rawData : foodList) {
                try {
                    var raw = rawData.split(":");
                    food.put(Material.valueOf(raw[0]), Double.valueOf(raw[1]));
                    plugin.sendLog(Level.CONFIG, "Food: " + raw[0] + "| Value: " + raw[1]);
                } catch (Exception e) {
                    plugin.sendLog(Level.WARNING, "Error on loading line '" + rawData + "' check it please.");
                }
            }
            plugin.sendLog("Food hashmap loaded");
        } else {
            throw new IllegalArgumentException("Food list is empty!");
        }
    }

    @Override
    public @NotNull HashMap<Material, Double> getFoodMap() {
        return new HashMap<>(food);
    }

    @Override
    public boolean hasFood(@NotNull Material material) {
        return food.containsKey(material);
    }

    @Override
    public double getValue(@NotNull Material material) {
        return food.get(material);
    }
}
