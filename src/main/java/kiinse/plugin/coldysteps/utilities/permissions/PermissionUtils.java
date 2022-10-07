package kiinse.plugin.coldysteps.utilities.permissions;

import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.enums.Permission;
import kiinse.plugin.coldysteps.utilities.ColdUtils;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionUtils {

    private final YamlFile config;
    private final ColdUtils coldUtils = new ColdUtils();

    public PermissionUtils(@NotNull DarkWaterJavaPlugin plugin) {
        this.config = plugin.getConfiguration();
    }

    public @NotNull Double addCold(@NotNull Player player) {
        return getAdding(player, coldUtils.getColdAdding(player));
    }

    private @NotNull Double getAdding(@NotNull Player player, double adding) {
        if (DarkPlayerUtils.hasPermission(player, Permission.COLDY_VIP))
            return adding - (adding*config.getDouble(Config.PERMISSION_VIP));
        if (DarkPlayerUtils.hasPermission(player, Permission.COLDY_PREMIUM))
            return adding - (adding*config.getDouble(Config.PERMISSION_PREMIUM));
        if (DarkPlayerUtils.hasPermission(player, Permission.COLDY_DELUXE))
            return adding - (adding*config.getDouble(Config.PERMISSION_DELUXE));
        return adding;
    }
}
