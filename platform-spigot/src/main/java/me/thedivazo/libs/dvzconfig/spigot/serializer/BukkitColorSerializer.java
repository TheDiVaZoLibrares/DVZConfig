package me.thedivazo.libs.dvzconfig.spigot.serializer;

import io.leangen.geantyref.TypeToken;
import me.thedivazo.libs.dvzconfig.core.serializer.ColorSerializer;
import org.bukkit.Color;

import java.util.Map;

public class BukkitColorSerializer extends ColorSerializer<Color> {
    public static final BukkitColorSerializer DEFAULT;
    static {
        DEFAULT = new BukkitColorSerializer(Map.ofEntries(
                Map.entry("black", Color.BLACK.asARGB()),
                Map.entry("aqua", Color.AQUA.asARGB()),
                Map.entry("blue", Color.BLUE.asARGB()),
                Map.entry("fuchsia", Color.FUCHSIA.asARGB()),
                Map.entry("gray", Color.GRAY.asARGB()),
                Map.entry("green", Color.GREEN.asARGB()),
                Map.entry("lime", Color.LIME.asARGB()),
                Map.entry("maroon", Color.MAROON.asARGB()),
                Map.entry("navy", Color.NAVY.asARGB()),
                Map.entry("olive", Color.OLIVE.asARGB()),
                Map.entry("yellow", Color.YELLOW.asARGB()),
                Map.entry("white", Color.WHITE.asARGB()),
                Map.entry("teal", Color.TEAL.asARGB()),
                Map.entry("silver", Color.SILVER.asARGB()),
                Map.entry("red", Color.RED.asARGB()),
                Map.entry("purple", Color.PURPLE.asARGB()),
                Map.entry("orange", Color.ORANGE.asARGB())
        ));
    }

    protected BukkitColorSerializer(Map<String, Integer> colorNamesModel) {
        super(TypeToken.get(Color.class), colorNamesModel);
    }

    @Override
    protected Color fromARGB(int argb) {
        return Color.fromARGB(argb);
    }

    @Override
    protected int toARGB(Color color) {
        return color.asARGB();
    }
}
