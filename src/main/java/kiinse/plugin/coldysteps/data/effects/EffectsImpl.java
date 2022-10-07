package kiinse.plugin.coldysteps.data.effects;

import kiinse.plugin.coldysteps.data.effects.interfaces.Effects;
import kiinse.plugin.coldysteps.enums.Config;
import kiinse.plugin.coldysteps.enums.File;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EffectsImpl extends YamlFile implements Effects {

    private final List<String> effects;

    public EffectsImpl(@NotNull DarkWaterJavaPlugin plugin) {
        super(plugin, File.EFFECTS_YML);
        effects = getStringList(Config.EFFECTS);
    }

    @Override
    public @NotNull List<String> getEffects() {
        return new ArrayList<>(effects);
    }
}
