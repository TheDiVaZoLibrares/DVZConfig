package me.thedivazo.libs.dvzconfig.core.serializer;

import io.leangen.geantyref.TypeToken;

import java.awt.*;
import java.util.Map;

//TODO: Сделать реализацию по умлочанию
public class AwtColorSerializer extends ColorSerializer<Color>{
    protected AwtColorSerializer(Map<String, Integer> colorNamesModel) {
        super(TypeToken.get(Color.class), colorNamesModel);
    }

    @Override
    protected Color fromARGB(int argb) {
        return new Color(argb, true);
    }

    @Override
    protected int toARGB(Color color) {
        return color.getRGB();
    }
}
