package me.thedivazo.libs.dvzconfig.spigot.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocationScalarSerializerTest {
    private LocationScalarSerializer serializer;

    @Mock
    private World mockWorld;

    @BeforeEach
    void setUp() {
        serializer = LocationScalarSerializer.DEFAULT;
        mockWorld = mock(World.class);
        when(mockWorld.getName()).thenReturn("world");
        Bukkit.setServer(mock(Server.class));
        when(Bukkit.getWorld("world")).thenReturn(mockWorld);
    }

    @Test
    void testSerialize() {
        Location location = new Location(mockWorld, -0.5, 64.0, -5.25, 0.0f, .5f);
        String expected = "world:-.5:+64.0:-5.25|0.:+.5";
        Object serialized = serializer.serialize(location, type -> true);
        assertEquals(expected, serialized);
    }

    @Test
    void testSerializeNoRotation() {
        Location location = new Location(mockWorld, 10.5, 64.0, -5.25);
        String expected = "world:10.5:64.0:-5.25";
        Object serialized = serializer.serialize(location, type -> true);
        assertEquals(expected, serialized);
    }

    @Test
    void testSerializeNoWorld() {
        Location location = new Location(null, 10.5, 64.0, -5.25, 90f, 0.5f);
        String expected = "10.5:64:-5.25|90:0.5";
        Object serialized = serializer.serialize(location, type -> true);
        assertEquals(expected, serialized);
    }

    @Test
    void testSerializeNoRotationAndWorld() {
        Location location = new Location(null, 10.5, 64.0, -5.25);
        String expected = "10.5:64.0:-5.25";
        Object serialized = serializer.serialize(location, type -> true);
        assertEquals(expected, serialized);
    }

    @Test
    void testDeserialize() throws SerializationException {
        String input = "world:10.5:64.0:-5.25|90.0:45.0";
        Location location = serializer.deserialize(Location.class, input);
        assertNotNull(location);
        assertNotNull(location.getWorld());
        assertEquals(mockWorld.getName(), location.getWorld().getName());
        assertEquals(10.5, location.getX(), 0.0001);
        assertEquals(64.0, location.getY(), 0.0001);
        assertEquals(-5.25, location.getZ(), 0.0001);
        assertEquals(90.0f, location.getYaw(), 0.0001);
        assertEquals(45.0f, location.getPitch(), 0.0001);
    }

    @Test
    void testDeserializeNoRotation() throws SerializationException {
        String input = "world:10:64.0:-5.25";
        Location location = serializer.deserialize(Location.class, input);
        assertNotNull(location);
        assertNotNull(location.getWorld());
        assertEquals(mockWorld.getName(), location.getWorld().getName());
        assertEquals(10, location.getX(), 0.0001);
        assertEquals(64.0, location.getY(), 0.0001);
        assertEquals(-5.25, location.getZ(), 0.0001);
        assertEquals(0.0f, location.getYaw());
        assertEquals(0.0f, location.getPitch());
    }

    @Test
    void testDeserializeNoWorld() throws SerializationException {
        String input = "10.5:64:-5.25";
        Location location = serializer.deserialize(Location.class, input);
        assertNotNull(location);
        assertNull(location.getWorld());
        assertEquals(10.5, location.getX(), 0.0001);
        assertEquals(64.0, location.getY(), 0.0001);
        assertEquals(-5.25, location.getZ(), 0.0001);
        assertEquals(0.0f, location.getYaw());
        assertEquals(0.0f, location.getPitch());
    }

    @Test
    void testDeserializeNoRotationAndWorld() throws SerializationException {
        String input = "10.5:64.0:-5.25";
        Location location = serializer.deserialize(Location.class, input);
        assertNotNull(location);
        assertNull(location.getWorld());
        assertEquals(10.5, location.getX(), 0.0001);
        assertEquals(64.0, location.getY(), 0.0001);
        assertEquals(-5.25, location.getZ(), 0.0001);
        assertEquals(0.0f, location.getYaw());
        assertEquals(0.0f, location.getPitch());
    }

    @Test
    void testDeserializeInvalidString() {
        String invalidInput = "invalid_location_string";
        assertThrows(SerializationException.class, () -> serializer.deserialize(Location.class, invalidInput));
    }

    @Test
    void testDeserializeInvalidCoordinates() {
        String invalidInput = "world:abc:64.0:-5.25";
        assertThrows(NumberFormatException.class, () -> serializer.deserialize(Location.class, invalidInput));
    }
}