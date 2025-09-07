package com.innowise.model;

import com.innowise.config.SimulationConfig;
import com.innowise.model.part.PartType;
import com.innowise.model.part.RobotPart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Phaser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Faction behavior and robot assembly tests")
public class FactionTest {

    @Test
    @DisplayName("Should return correct quantity of robots")
    void shouldCalculateCompleteRobotCountCorrectly() {
        Factory mockFactory = mock(Factory.class);
        Phaser phaser = new Phaser();

        Faction faction = new Faction("TestFaction", mockFactory, phaser);

        faction.getInventory().addAll(List.of(
                new RobotPart(PartType.HEAD),
                new RobotPart(PartType.HAND),
                new RobotPart(PartType.HAND),
                new RobotPart(PartType.FEET),
                new RobotPart(PartType.FEET),
                new RobotPart(PartType.TORSO)));


        assertEquals(1, faction.getCompleteRobotCount());
    }

    @Test
    @DisplayName("Should return correct quantity of robot parts")
    void factionShouldCollectPartsEachDay() throws InterruptedException {
        Factory mockFactory = mock(Factory.class);
        Phaser phaser = new Phaser();


        when(mockFactory.collectParts(anyInt()))
                .thenReturn(List.of(
                        new RobotPart(PartType.HEAD),
                        new RobotPart(PartType.FEET),
                        new RobotPart(PartType.HAND),
                        new RobotPart(PartType.TORSO),
                        new RobotPart(PartType.FEET)
                ));

        Faction faction = new Faction("Collector", mockFactory, phaser);

        Thread thread = new Thread(faction);
        thread.start();
        thread.join();

        int expectedParts = SimulationConfig.DAYS * SimulationConfig.FACTION_PARTS_PER_DAY;
        assertEquals(expectedParts, faction.getInventory().size());
    }
}
