package kiinse.plugin.coldysteps.data.cold.interfaces;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerCold {

    boolean hasPlayer(@NotNull Player player);

    void putPlayer(@NotNull Player player, double value);

    void updatePlayer(@NotNull Player player, double value);

    double getPlayerValue(@NotNull Player player);

    double getPlayerValue(@NotNull UUID uuid);

    void addToPlayer(@NotNull Player player, double value);

    void removeFromPlayer(@NotNull Player player, double value);

    void restorePlayer(@NotNull Player player);

    void save() throws Exception;

}
