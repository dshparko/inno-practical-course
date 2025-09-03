import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Faction implements Runnable {

    private final String name;
    private final Factory factory;

    private final Queue<RobotPart> inventory = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        for (int day = 0; day < 100; day++) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(5, 15));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            List<RobotPart> collected = factory.collectParts(5);
            inventory.addAll(collected);

            System.out.printf("Day %d. Faction %s collected %d parts%n", day + 1, name, collected.size());
        }
    }

    public int getCompleteRobotCount() {
        var counts = inventory.stream()
                .collect(Collectors.groupingBy(RobotPart::type, Collectors.counting()));

        return Arrays.stream(PartType.values())
                .mapToInt(type -> counts.getOrDefault(type, 0L).intValue())
                .min()
                .orElse(0);
    }
}
