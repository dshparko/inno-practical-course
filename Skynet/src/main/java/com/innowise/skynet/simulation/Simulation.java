package com.innowise.skynet.simulation;

import com.innowise.skynet.model.Faction;
import com.innowise.skynet.model.Factory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class Simulation {
    private final Phaser phaser;
    private final Factory factory;
    private final List<Faction> factions;
    private final ExecutorService executor;

    public Simulation(List<String> factionNames) {
        this.phaser = new Phaser();
        this.factory = new Factory(phaser);

        this.factions = factionNames.stream()
                .map(name -> new Faction(name, factory, phaser))
                .toList();

        this.executor = Executors.newFixedThreadPool(factions.size() + 1);
    }

    public void start() {
        executor.submit(factory);
        factions.forEach(executor::submit);
    }

    public void shutdownAndAwait() {

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                log.warn("Executor did not terminate in time — forcing shutdown");
                List<Runnable> droppedTasks = executor.shutdownNow();
                log.warn("Dropped {} tasks during forced shutdown", droppedTasks.size());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Simulation interrupted during shutdown");
            executor.shutdownNow();
        }

    }

    public Faction getChamp() {
        return factions.stream()
                .max(Comparator.comparingInt(Faction::getCompleteRobotCount))
                .orElse(null);
    }
}