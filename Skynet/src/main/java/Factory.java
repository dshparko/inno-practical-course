import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Factory {
    private List<RobotPart> dailyParts = new ArrayList<>();
    private final Random random = new Random();

    public void producePart() {
        dailyParts.clear();
        int count = random.nextInt(11);
        for (int i = 0; i < count; i++) {
            dailyParts.add(getRandomPart());
        }
    }

    private RobotPart getRandomPart() {
        return new RobotPart(PartType.values()[random.nextInt(4)]);
    }
}
