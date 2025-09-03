import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Factory {
    private final Queue<RobotPart> availableParts = new ConcurrentLinkedQueue<>();

    public void produceParts(int maxCount) {
        availableParts.clear();
        int count = ThreadLocalRandom.current().nextInt(1, maxCount + 1);

        for (int i = 0; i < count; i++) {
            availableParts.add(getRandomRobotPart());
        }
    }

    public List<RobotPart> collectParts(int maxCount) {
        List<RobotPart> collected = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            RobotPart part = availableParts.poll();
            if (part == null) break;

            collected.add(part);
        }
        return collected;
    }

    private RobotPart getRandomRobotPart() {
        return new RobotPart(PartType.getRandomPart());
    }
}
