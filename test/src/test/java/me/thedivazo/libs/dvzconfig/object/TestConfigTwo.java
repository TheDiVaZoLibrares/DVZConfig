package me.thedivazo.libs.dvzconfig.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 06.02.2025
 */
@ConfigSerializable
@NoArgsConstructor
@Getter
@Setter
public class TestConfigTwo {
    private int countAnimals = 3;
    private Animal animal = new Bug(){{
        setAge(3);
        setName("Ahmed");
        setCountLegs(4);
    }};
}
