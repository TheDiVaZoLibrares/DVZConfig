package me.thedivazo.libs.dvzconfig.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Set;

/**
 * @author TheDiVaZo
 * created on 05.02.2025
 */
@NoArgsConstructor
@Getter
@Setter
public class Animal {
    private String name;
    private int age;
}
