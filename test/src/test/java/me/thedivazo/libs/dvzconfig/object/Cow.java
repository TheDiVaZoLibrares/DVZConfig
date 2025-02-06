package me.thedivazo.libs.dvzconfig.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 05.02.2025
 */
@NoArgsConstructor
@Setter
@Getter
public class Cow extends Animal {
    boolean isPregnant;
}
