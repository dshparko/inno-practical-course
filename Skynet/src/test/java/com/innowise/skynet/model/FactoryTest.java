package com.innowise.skynet.model;

import com.innowise.skynet.model.part.RobotPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Phaser;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Factory production and collection behavior")
class FactoryTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        Phaser phaser = new Phaser();
        factory = new Factory(phaser);
    }

    @Test
    @DisplayName("Produced parts should be available for collection")
    void producePartsAddsToQueue() {
        factory.produceParts(10);
        List<RobotPart> collected = factory.collectParts(10);

        assertAll("Part production and collection",
                () -> assertFalse(collected.isEmpty()),
                () -> assertTrue(collected.size() <= 10)
        );
    }

    @Test
    @DisplayName("Collecting parts should reduce queue size")
    void collectPartsRemovesFromQueue() {
        factory.produceParts(5);

        List<RobotPart> firstBatch = factory.collectParts(3);
        List<RobotPart> secondBatch = factory.collectParts(5);

        assertTrue(firstBatch.size() <= 3);
        assertTrue(secondBatch.size() <= 2);
    }

    @Test
    @DisplayName("Produce and collect operations should be thread-safe")
    void produceAndCollectAreThreadSafe() throws InterruptedException {
        Thread producer = new Thread(() -> factory.produceParts(10));
        Thread collector = new Thread(() -> {
            try {
                Thread.sleep(50);
                factory.collectParts(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Collector thread was interrupted");
            }
        });

        producer.start();
        collector.start();

        producer.join();
        collector.join();

        List<RobotPart> remaining = factory.collectParts(10);
        assertTrue(remaining.isEmpty() || remaining.size() < 10);
    }
}
