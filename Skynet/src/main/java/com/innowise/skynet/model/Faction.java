package com.innowise.skynet.model;

import com.innowise.skynet.config.SimulationConfig;
import com.innowise.skynet.model.part.PartType;
import com.innowise.skynet.model.part.RobotPart;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;

import static com.innowise.skynet.config.SimulationConfig.FACTION_PARTS_PER_DAY;
import static com.innowise.skynet.config.SimulationConfig.SLEEP_MS;


@Slf4j
@RequiredArgsConstructor
public class Faction implements Runnable {

    @Getter
    private final String name;
    private final Factory factory;
    private final Phaser phaser;

    @Getter
    private final Queue<RobotPart> inventory = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        phaser.register();
        for (int day = 1; day <= SimulationConfig.DAYS; day++) {
            productionPhase();

            collectionPhase(day);

            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void productionPhase() {
        phaser.arriveAndAwaitAdvance();
    }

    private void collectionPhase(int day) {
        List<RobotPart> collected = factory.collectParts(FACTION_PARTS_PER_DAY);
        inventory.addAll(collected);
        logCollection(day, collected.size());
        phaser.arriveAndAwaitAdvance();
    }

    public int getCompleteRobotCount() {
        Map<PartType, Long> counts = inventory.stream()
                .collect(Collectors.groupingBy(RobotPart::type, Collectors.counting()));

        return Arrays.stream(PartType.values())
                .mapToInt(type -> counts.getOrDefault(type, 0L).intValue())
                .min()
                .orElse(0);
    }


    private void logCollection(int day, int collectedCount) {
        log.info("Day {}. Faction {} collected {} parts", day, name, collectedCount);
    }
}
