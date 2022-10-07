package kiinse.plugin.coldysteps.utilities;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.blocks.interfaces.Blocks;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.data.food.interfaces.Food;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ColdUtils {

    private final ColdySteps coldySteps = ColdySteps.getInstance();
    private final PlayerCold cold = coldySteps.getCold();
    private final YamlFile config = coldySteps.getConfiguration();
    private final Food food = coldySteps.getFood();
    private final Blocks blocks = coldySteps.getBlocks();

    public void addColdPlayer(@NotNull Player player) {
        if (!cold.hasPlayer(player)) cold.putPlayer(player, 0.0);
    }

    public void removeCold(@NotNull Player player) {
        cold.removeFromPlayer(player, getRemoveByFire(player) + getRemoveByLava(player) + getRemoveByHeatingBlocks(player));
    }

    public double getColdAdding(@NotNull Player player) {
        return (getAddingValue(player)) - (config.getDouble(Config.ADD_LEATHER) * getLeatherCount(player));
    }

    public double getColdRemoving(@NotNull Player player) {
        return config.getDouble(Config.REMOVE_OUTBIOME) + (config.getDouble(Config.REMOVE_LEATHER) * getLeatherCount(player));
    }

    public void restoreByFood(@NotNull PlayerItemConsumeEvent event, @NotNull ItemStack item) {
        var material = item.getType();
        if (food.hasFood(material)) cold.removeFromPlayer(event.getPlayer(), food.getValue(material));
    }

    private double getRemoveByHeatingBlocks(@NotNull Player player) {
        var radius = config.getInt(Config.REMOVE_RADIUS);
        var location = player.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        var world = player.getWorld();
        var value = 0.0;
        for (var block : DarkUtils.getRegionBlocks(player.getWorld(), new Location(world, (double) x-radius, (double) y-radius, (double) z-radius), new Location(world, (double) x+radius, (double) y+radius, (double) z+radius))) {
            if (blocks.hasBlock(block)) {
                var blockValue = blocks.getValue(block);
                if (blockValue > value) value = blockValue;
            }
        }
        return value;
    }

    private double getAddingValue(@NotNull Player player) {
        return getAddByWaterOrSnow(player) + getAddByThunder(player) + config.getDouble(Config.ADD_INBIOME);
    }

    private double getRemoveByFire(@NotNull Player player) {
        if (player.getFireTicks() > 10)
            return config.getDouble(Config.REMOVE_FIRE);
        return 0.0;
    }

    private double getRemoveByLava(@NotNull Player player) {
        if (DarkPlayerUtils.isInLava(player))
            return config.getDouble(Config.REMOVE_LAVA);
        return 0.0;
    }

    private double getAddByWaterOrSnow(@NotNull Player player) {
        if (player.isInWater() || player.getFreezeTicks() > 130)
            return config.getDouble(Config.ADD_WATER);
        return 0;
    }

    private double getAddByThunder(@NotNull Player player) {
        var world = player.getWorld();
        if (world.isThundering()) {
            var location = player.getLocation();
            if (world.getHighestBlockYAt(location) < location.getBlockY())
                return config.getDouble(Config.ADD_THUNDER);
        }
        return 0;
    }

    private int getLeatherCount(@NotNull Player player) {
        int count = 0;
        for (var armor : player.getInventory().getArmorContents()) {
            if (armor != null && leatherArmorCheck(armor)) count++;
        }
        return count;
    }

    private boolean leatherArmorCheck(@NotNull ItemStack armor) {
        return switch (armor.getType()) {
            case LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_LEGGINGS -> true;
            default -> false;
        };
    }
}
