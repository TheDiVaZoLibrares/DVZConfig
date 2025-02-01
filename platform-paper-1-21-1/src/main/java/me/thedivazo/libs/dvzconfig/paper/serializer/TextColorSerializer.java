package me.thedivazo.libs.dvzconfig.paper.serializer;

import io.leangen.geantyref.TypeToken;
import io.papermc.paper.text.PaperComponents;
import me.thedivazo.libs.dvzconfig.core.serializer.ColorSerializer;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.Map;

//TODO: Сделать реализацию по умлочанию
public class TextColorSerializer extends ColorSerializer<TextColor> {
    public TextColorSerializer(Map<String, Integer> colorNamesModel) {
        super(TypeToken.get(TextColor.class), colorNamesModel);
    }

    @Override
    protected TextColor fromARGB(int argb) {
        return TextColor.color(argb);
    }

    @Override
    protected int toARGB(TextColor color) {
        return color.value();
    }
}
