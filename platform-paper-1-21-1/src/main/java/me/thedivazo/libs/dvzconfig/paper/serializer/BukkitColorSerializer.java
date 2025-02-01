package me.thedivazo.libs.dvzconfig.paper.serializer;

import io.leangen.geantyref.TypeToken;
import me.thedivazo.libs.dvzconfig.core.serializer.ColorSerializer;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

import java.util.Map;

public class BukkitColorSerializer extends ColorSerializer<Color> {
    protected BukkitColorSerializer(Map<String, Integer> colorNamesModel) {
        super(TypeToken.get(Color.class), colorNamesModel);
    }

    @Override
    protected Color fromRgba(int rgba) {
        return Color.fromRGB(rgba);
    }

    @Override
    protected int fromColor(Color color) {
        return color.asARGB();
    }
}
