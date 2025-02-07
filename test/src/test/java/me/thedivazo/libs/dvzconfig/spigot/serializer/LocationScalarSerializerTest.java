/*
 * This file is part of DVZConfig, licensed under the Apache License 2.0.
 *
 *  Copyright (c) TheDiVaZo <thedivazoyoutub@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.thedivazo.libs.dvzconfig.spigot.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationScalarSerializerTest {
    private LocationScalarSerializer serializer;

    private MockedStatic<Bukkit> mockBukkit;

    @Mock
    private World mockWorld;

    @Mock
    private Server mockServer;

    @BeforeEach
    void setUp() {
        serializer = LocationScalarSerializer.DEFAULT;

        mockBukkit = mockStatic(Bukkit.class);

        mockServer = mock(Server.class);
        when(mockServer.getLogger()).thenReturn(Logger.getGlobal());

        mockWorld = mock(World.class);
        when(mockWorld.getName()).thenReturn("world");
        when(Bukkit.getServer()).thenReturn(mockServer);
        when(Bukkit.getWorld("world")).thenReturn(mockWorld);
    }

    @AfterEach
    public void tearDown() {
        mockBukkit.close();
    }

    @Test
    void hasBukkitMocked() {
        // Ensure that the static mock for UserService is registered
        assertTrue(mockingDetails(Bukkit.class).isMock());
    }

    @Test
    void testSerialize() {
        Location location = new Location(mockWorld, -0.5, 64.0, -5.25, 0.0f, .5f);
        String expected = "world:-0.5:64:-5.25|0:0.5";
        Object serialized = serializer.serialize(location, type -> true);
        assertEquals(expected, serialized);
    }

    @Test
    void testSerializeNoRotation() {
        Location location = new Location(mockWorld, 10.5, 64.0, -5.25);
        String expected = "world:10.5:64:-5.25";
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
        String expected = "10.5:64:-5.25";
        Object serialized = serializer.serialize(location, type -> true);
        assertEquals(expected, serialized);
    }

    @Test
    void testDeserialize() throws SerializationException {
        String input = "world:10.5:64.0:-5.25|90.0:-.5";
        Location location = serializer.deserialize(Location.class, input);
        assertNotNull(location);
        assertNotNull(location.getWorld());
        assertEquals(mockWorld.getName(), location.getWorld().getName());
        assertEquals(10.5, location.getX(), 0.0001);
        assertEquals(64.0, location.getY(), 0.0001);
        assertEquals(-5.25, location.getZ(), 0.0001);
        assertEquals(90.0f, location.getYaw(), 0.0001);
        assertEquals(-0.5f, location.getPitch(), 0.0001);
    }

    @Test
    void testDeserializeNoRotation() throws SerializationException {
        String input = "world:10:64.00:-5.25";
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
        String input = ".5:64.0:-5.25";
        Location location = serializer.deserialize(Location.class, input);
        assertNotNull(location);
        assertNull(location.getWorld());
        assertEquals(0.5, location.getX(), 0.0001);
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
        assertThrows(SerializationException.class, () -> serializer.deserialize(Location.class, invalidInput));
    }
}