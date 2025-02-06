package me.thedivazo.libs.dvzconfig.core.serializer.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * @author TheDiVaZo
 * created on 06.02.2025
 */
@NoArgsConstructor
@Getter
@Setter
public class UnknownAnimal extends Animal {
    private Set<String> unknownParam;
}
