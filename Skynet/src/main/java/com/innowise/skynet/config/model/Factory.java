package com.innowise.skynet.config.model;

import com.innowise.skynet.config.model.part.PartType;
import com.innowise.skynet.config.model.part.RobotPart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

import static com.innowise.skynet.config.SimulationConfig.DAYS;
import static com.innowise.skynet.config.SimulationConfig.FACTORY_PARTS_PER_DAY;
import static com.innowise.skynet.config.SimulationConfig.SLEEP_MS;

@Slf4j
@RequiredArgsConstructor
public class Factory implements Runnable {
    private final Queue<RobotPart> availableParts = new ConcurrentLinkedQueue<>();
    private final Phaser phaser;

    @Override
    public void run() {
        phaser.register();
        for (int day = 1; day <= DAYS; day++) {
            productionPhase();
            collectionPhase();
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        phaser.arriveAndDeregister();
    }
    public void produceParts(int maxCount) {
        int count = getRandomCount(maxCount);

        for (int i = 0; i < count; i++) {
            synchronized (availableParts) {
                availableParts.add(getRandomRobotPart());
            }
        }

        log.info("Factory: produced parts quantity is " + count);
        log.info("Factory: available parts quantity is " + availableParts.size());
    }

    public List<RobotPart> collectParts(int maxCount) {
        int count = getRandomCount(maxCount);
        List<RobotPart> collected = new ArrayList<>();
        while (!availableParts.isEmpty() && count > 0) {
            RobotPart part;
            synchronized (availableParts) {
                part = availableParts.poll();
            }
            if (part == null) {
                break;
            }

            collected.add(part);
            count--;
        }
        return collected;
    }

    private int getRandomCount(int maxCount) {
        return ThreadLocalRandom.current().nextInt(1, maxCount + 1);
    }

    private RobotPart getRandomRobotPart() {
        return new RobotPart(PartType.getRandomPart());
    }

    private void productionPhase() {
        produceParts(FACTORY_PARTS_PER_DAY);
        phaser.arriveAndAwaitAdvance();
    }

    private void collectionPhase() {
        phaser.arriveAndAwaitAdvance();
    }


}
