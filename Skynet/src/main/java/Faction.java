import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Faction implements Runnable {

    @Getter
    private final String name;
    private final Factory factory;

    private final Queue<RobotPart> inventory = new ConcurrentLinkedQueue<>();


    @Override
    public void run() {
        for (int day = 1; day <= SimulationConfig.DAYS; day++) {
            sleepRandomInterval();

            List<RobotPart> collected = factory.collectParts(SimulationConfig.FACTION_PARTS_PER_DAY);
            inventory.addAll(collected);

            logCollection(day, collected.size());
        }
    }

    public int getCompleteRobotCount() {
        Map<PartType, Long> counts = inventory.stream()
                .collect(Collectors.groupingBy(RobotPart::type, Collectors.counting()));

        return Arrays.stream(PartType.values())
                .mapToInt(type -> counts.getOrDefault(type, 0L).intValue())
                .min()
                .orElse(0);
    }

    private void sleepRandomInterval() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(SimulationConfig.MIN_SLEEP_MS, SimulationConfig.MAX_SLEEP_MS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void logCollection(int day, int collectedCount) {
        System.out.printf("Day %d. Faction %s collected %d parts%n", day, name, collectedCount);
    }
}
