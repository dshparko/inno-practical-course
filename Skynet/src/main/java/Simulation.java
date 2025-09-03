import java.util.List;
import java.util.concurrent.*;

public class Simulation {

    public static void main(String[] args) throws InterruptedException {
        Factory factory = new Factory();

        List<Faction> factions = List.of(
                new Faction("World", factory),
                new Faction("Wednesday", factory)
        );

        SimulationRunner runner = new SimulationRunner(factory, factions);
        runner.run();
    }
}
