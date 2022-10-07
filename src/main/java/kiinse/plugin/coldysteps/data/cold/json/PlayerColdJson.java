package kiinse.plugin.coldysteps.data.cold.json;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.enums.File;
import kiinse.plugins.darkwaterapi.api.exceptions.JsonFileException;
import kiinse.plugins.darkwaterapi.api.files.filemanager.JsonFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerColdJson implements PlayerCold {

    private final JSONObject coldJson;
    private final JsonFile json;
    private final ColdySteps coldySteps;

    public PlayerColdJson(@NotNull ColdySteps coldySteps) throws JsonFileException {
        this.coldySteps = coldySteps;
        this.json = new JsonFile(coldySteps, File.DATA_JSON);
        coldJson = json.getJsonFromFile();
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return coldJson.has(player.getUniqueId().toString());
    }

    @Override
    public void putPlayer(@NotNull Player player, double value) {
        coldJson.put(player.getUniqueId().toString(), value);
        coldySteps.sendLog(Level.CONFIG, "Player '" + player.getUniqueId() + "' saved to json");
    }

    @Override
    public void updatePlayer(@NotNull Player player, double value) {
        coldJson.remove(player.getUniqueId().toString());
        coldJson.put(player.getUniqueId().toString(), value);
    }

    @Override
    public double getPlayerValue(@NotNull Player player) throws IllegalArgumentException {
        if (coldJson.has(player.getUniqueId().toString()))
            return coldJson.getDouble(player.getUniqueId().toString());
        throw new IllegalArgumentException("Player not found");
    }

    @Override
    public double getPlayerValue(@NotNull UUID uuid) {
        if (coldJson.has(uuid.toString()))
            return coldJson.getDouble(uuid.toString());
        throw new IllegalArgumentException("Player not found");
    }

    @Override
    public void addToPlayer(@NotNull Player player, double value) throws IllegalArgumentException {
        if (hasPlayer(player)) {
            updatePlayer(player, Math.min(getPlayerValue(player) + value, 120.0));
            return;
        }
        throw new IllegalArgumentException("Player not found");
    }

    @Override
    public void removeFromPlayer(@NotNull Player player, double value) throws IllegalArgumentException {
        if (hasPlayer(player)) {
            updatePlayer(player, Math.max(getPlayerValue(player) - value, 0.0));
            return;
        }
        throw new IllegalArgumentException("Player not found");
    }

    @Override
    public void restorePlayer(@NotNull Player player) {
        updatePlayer(player, 0.0);
        coldySteps.sendLog(Level.CONFIG, "Player '" + player.getUniqueId() + "' cold restored");
    }

    @Override
    public void save() throws Exception {
        json.saveJsonToFile(coldJson);
        coldySteps.sendLog(Level.CONFIG, "JsonObject saved to file");
    }

}
