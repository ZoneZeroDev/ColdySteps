package kiinse.plugin.coldysteps.data.cold.sql;

import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.cold.sql.database.tables.Players;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.exceptions.ColdyException;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerColdSql implements PlayerCold {

    private final Connection connection;
    private final DSLContext context;
    private final ColdySteps coldySteps;

    public PlayerColdSql(@NotNull ColdySteps coldySteps) {
        this.coldySteps = coldySteps;
        context = coldySteps.getColdSQL().getContext();
        connection = coldySteps.getColdSQL().getConnection();
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return context.select(Players.PLAYERS.fields())
                .from(Players.PLAYERS)
                .where(Players.PLAYERS.UUID.equal(player.getUniqueId()))
                .fetch().isNotEmpty();
    }

    @Override
    public void putPlayer(@NotNull Player player, double value) {
        var newRecord = context.newRecord(Players.PLAYERS);
        newRecord.set(Players.PLAYERS.UUID, player.getUniqueId());
        newRecord.set(Players.PLAYERS.NAME, DarkPlayerUtils.getPlayerName(player));
        newRecord.set(Players.PLAYERS.VALUE, value);
        newRecord.store();
        coldySteps.sendLog(Level.CONFIG, "Player '" + player.getUniqueId() + "' saved to database");
    }

    @Override
    public void updatePlayer(@NotNull Player player, double value) {
        context.update(Players.PLAYERS)
                .set(Players.PLAYERS.VALUE, value)
                .where(Players.PLAYERS.UUID.eq(player.getUniqueId()))
                .execute();
    }

    @Override
    public double getPlayerValue(@NotNull Player player) throws IllegalArgumentException {
        return context.select(Players.PLAYERS.VALUE)
                .from(Players.PLAYERS)
                .where(Players.PLAYERS.UUID.equal(player.getUniqueId()))
                .fetch().get(0).component1();
    }

    @Override
    public double getPlayerValue(@NotNull UUID uuid) {
        return context.select(Players.PLAYERS.VALUE)
                .from(Players.PLAYERS)
                .where(Players.PLAYERS.UUID.equal(uuid))
                .fetch().get(0).component1();
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
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                coldySteps.sendLog(Level.CONFIG, "Postgresql connection closed");
            }
        } catch (SQLException e) {
            throw new ColdyException(e);
        }
    }
}
