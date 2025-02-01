package me.thedivazo.libs.dvzconfig.core.serializer;

import io.leangen.geantyref.TypeToken;

import java.awt.*;
import java.util.Map;

public class AwtColorSerializer extends ColorSerializer<Color>{
    protected AwtColorSerializer(Map<String, Integer> colorNamesModel) {
        super(TypeToken.get(Color.class), colorNamesModel);
    }

    @Override
    protected Color fromRgba(int rgba) {
        return new Color(rgba);
    }

    @Override
    protected int fromColor(Color color) {
        return color.getRGB();
    }
}
