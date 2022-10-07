package kiinse.plugin.coldysteps.data.food.interfaces;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("unused")
public interface Food {

    void load() throws IllegalArgumentException;

    @NotNull HashMap<Material, Double> getFoodMap();

    boolean hasFood(@NotNull Material material);

    double getValue(@NotNull Material material);
}
