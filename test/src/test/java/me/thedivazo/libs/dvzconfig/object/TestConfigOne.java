package me.thedivazo.libs.dvzconfig.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;
import java.util.Set;

/**
 * @author TheDiVaZo
 * created on 06.02.2025
 */
@ConfigSerializable
@Setter
@Getter
@NoArgsConstructor
public class TestConfigOne {
    private String zooName = "moscow zoo";

    private Cow cow = new Cow(){{
        setAge(5);
        setName("Cow");
        setPregnant(true);
    }};

    private Map<String, Animal> map = Map.of(
            "papapa",
            new Cow() {{
                setPregnant(true);
                setAge(2);
                setName("Papapa");
            }},
            "pepepe",
            new Cat() {{
                setAge(5);
                setName("Pepepe");
                setColor("red");
            }});
}
