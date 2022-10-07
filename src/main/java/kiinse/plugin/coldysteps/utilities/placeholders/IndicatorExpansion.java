package kiinse.plugin.coldysteps.utilities.placeholders;

import com.google.common.base.Strings;
import kiinse.plugin.coldysteps.ColdySteps;
import kiinse.plugin.coldysteps.data.cold.interfaces.PlayerCold;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.enums.Message;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.api.files.locale.PlayerLocale;
import kiinse.plugins.darkwaterapi.api.files.locale.PlayerLocales;
import kiinse.plugins.darkwaterapi.api.files.messages.Messages;
import kiinse.plugins.darkwaterapi.core.utilities.DarkUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IndicatorExpansion extends PlaceholderExpansion {

    private final ColdySteps coldySteps;
    private final PlayerCold cold;
    private final YamlFile config;
    private final PlayerLocales locales;
    private final Messages messages;

    // %coldy_cold%
    // %coldy_numeric%
    // %coldy_status%
    // %coldy_indicator%
    // %coldy_indicator_simple%

    public IndicatorExpansion(@NotNull ColdySteps coldySteps){
        this.coldySteps = coldySteps;
        this.cold = coldySteps.getCold();
        this.config = coldySteps.getConfiguration();
        this.messages = coldySteps.getMessages();
        this.locales = coldySteps.getLocales();
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getAuthor(){
        return coldySteps.getDescription().getAuthors().get(0);
    }


    @Override
    public @NotNull String getIdentifier(){
        return "coldy";
    }

    @Override
    public @NotNull String getVersion(){
        return coldySteps.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier){
        if(player == null) return "";
        var coldy = cold.getPlayerValue(player);
        var locale = locales.getLocale(player);
        return switch (identifier) {
            case "cold" -> messages.getStringMessage(locale, Message.PH_COLD);
            case "numeric" -> numericFunc(coldy);
            case "status" -> statusFunc(locale, coldy);
            case "indicator_simple" -> indicatorSimpleFunc(coldy);
            case "indicator" -> indicatorFunc(coldy);
            default -> null;
        };
    }

    private @NotNull String numericFunc(double cold) {
        if (cold >= 100.0) {
            return "100";
        }
        return String.valueOf(((Double) cold).intValue());
    }

    private @NotNull String statusFunc(@NotNull PlayerLocale locale, double cold) {
        if (cold >= 100.0) {
            return messages.getStringMessage(locale, Message.PH_CRITICAL_COLD);
        }
        if (cold < 100.0 && cold >= 70.0) {
            return messages.getStringMessage(locale, Message.PH_HARD_COLD);
        }
        if (cold < 70.0 && cold >= 40.0) {
            return messages.getStringMessage(locale, Message.PH_MEDIUM_COLD);
        }
        if (cold < 40.0 && cold >= 5.0) {
            return messages.getStringMessage(locale, Message.PH_LIGHT_COLD);
        }
        if (cold < 5.0) {
            return messages.getStringMessage(locale, Message.PH_NO_COLD);
        }
        return "NaN";
    }

    private @NotNull String indicatorSimpleFunc(double cold) {
        if (cold >= 100.0) {
            return ChatColor.DARK_RED + "----------";
        } else if (cold <= 0.0) {
            return ChatColor.WHITE + "----------";
        } else if (cold >= 90.0) {
            return getProgressBar((int)cold, ChatColor.RED);
        }
        return getProgressBar((int)cold, ChatColor.WHITE);
    }

    private @NotNull String indicatorFunc(double cold) {
        if (cold > 0) {
            if (cold >= 100.0) return DarkUtils.colorize(config.getString(Config.INDICATOR_FULL));
            return getIndicator(((Double) cold).intValue());
        }
        return "";
    }

    private @NotNull String getProgressBar(int current, @NotNull ChatColor completedColor) {
        int progressBars = (int) (10 * ((float) current / 100));
        return Strings.repeat("" + completedColor + '-', progressBars)
                + Strings.repeat("" + ChatColor.DARK_GRAY + '-', 10 - progressBars);
    }

    private @NotNull String getIndicator(int cold) {
        return DarkUtils.colorize(DarkUtils.replaceWord(config.getString(Config.INDICATOR_FORMAT), "{INDICATOR}", DarkUtils.getProgressBar(cold, 100, 10, Objects.requireNonNull(config.getString(Config.INDICATOR_CHAR_FIRST)), Objects.requireNonNull(config.getString(Config.INDICATOR_CHAR_SECOND)))));
    }
}
