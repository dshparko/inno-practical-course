package com.innowise.skynet.simulation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Simulation lifecycle and production tests")
public class SimulationTest {

    @Test
    @DisplayName("Each faction should produce at least one robot")
    void factionsShouldProduceRobots() {
        Simulation simulation = new Simulation(List.of("World", "Wednesday"));

        runSimulation(simulation);

        simulation.getFactions().forEach(faction ->
                assertTrue(faction.getCompleteRobotCount() > 0,
                        () -> "Faction '%s' produced no robots".formatted(faction.getName()))
        );
    }

    @Test
    @DisplayName("Executor should shut down and terminate correctly")
    void executorShouldShutdownAndTerminate() {
        Simulation simulation = new Simulation(List.of("World"));

        runSimulation(simulation);

        assertAll("Executor shutdown checks",
                () -> assertTrue(simulation.getExecutor().isShutdown()),
                () -> assertTrue(simulation.getExecutor().isTerminated())
        );
    }

    @Test
    @DisplayName("Should return the champ")
    void shouldReturnTheChamp() {
        Simulation simulation = new Simulation(List.of("World"));

        runSimulation(simulation);
        assertNotNull(simulation.getChamp());
        assertEquals("World", simulation.getChamp().getName());
    }

    private void runSimulation(Simulation simulation) {
        simulation.start();
        simulation.shutdownAndAwait();
    }

}
