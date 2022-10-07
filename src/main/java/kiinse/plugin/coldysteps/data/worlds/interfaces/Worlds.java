package kiinse.plugin.coldysteps.data.worlds.interfaces;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public interface Worlds {

    void load() throws IllegalArgumentException;

    @NotNull List<String> getWorldsList();

    boolean hasWorld(@NotNull World world);

    boolean isPlayerInAllowedWorld(@NotNull Player player);
}
