package me.thedivazo.libs.dvzconfig.core.serializer;

import me.thedivazo.libs.dvzconfig.object.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassForFieldSerializerTest {
    private static ClassForFieldSerializer<String, Animal> animalNameSerializer;

    @BeforeAll
    static void setUp() {
        animalNameSerializer = new ClassForFieldSerializer<>(
                new Object[]{"type"},
                String.class,
                Map.of(
                        "bug", Bug.class,
                        "cat", Cat.class,
                        "cow", Cow.class
                )
        );
    }

    @Test
    void testDeserializeSuccess() throws SerializationException {
        ConfigurationNode node = createNode("type: cat\nname: valera\ncolor: black\nage: 3");
        Animal animal = animalNameSerializer.deserialize(Animal.class, node);
        assertInstanceOf(Cat.class, animal);
        assertEquals("valera", animal.getName());
        assertEquals(3, animal.getAge());
        assertEquals("black", ((Cat) animal).getColor());
    }

    @Test
    void testDeserializeMissingTypeField() {
        ConfigurationNode node = createNode("age: 3");
        assertThrows(SerializationException.class, () -> animalNameSerializer.deserialize(Animal.class, node));
    }

    @Test
    void testDeserializeUnknownType() {
        ConfigurationNode node = createNode("type: dog\nname: petrov\nage: 5");
        assertThrows(SerializationException.class, () -> animalNameSerializer.deserialize(Animal.class, node));
    }

    @Test
    void testSerializeSuccess() throws SerializationException {
        Cat cat = new Cat();
        cat.setName("barsic");
        cat.setAge(3);
        cat.setColor("black");
        ConfigurationNode node = YamlConfigurationLoader.builder().build().createNode();
        animalNameSerializer.serialize(Animal.class, cat, node);
        assertEquals("cat", node.node("type").getString());
        assertEquals("barsic", node.node("name").getString());
        assertEquals(3, node.node("age").getInt());
        assertEquals("black", node.node("color").getString());
    }

    @Test
    void testSerializeWrongInstance() {
        UnknownAnimal animal = new UnknownAnimal();
        animal.setUnknownParam(Set.of());
        ConfigurationNode node = YamlConfigurationLoader.builder().build().createNode();
        assertThrows(SerializationException.class, () -> animalNameSerializer.serialize(Animal.class, animal, node));
    }

    private ConfigurationNode createNode(String yaml) {
        try {
            return YamlConfigurationLoader.builder().source(() -> new BufferedReader(new StringReader(yaml))).build().load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test configuration node", e);
        }
    }
}