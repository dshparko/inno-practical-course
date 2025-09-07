import com.innowise.config.SimulationConfig;
import com.innowise.model.Faction;
import com.innowise.model.Factory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class SimulationRunner {

    private final Factory factory;
    private final List<Faction> factions;

    public void run() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(factions.size() + 1);

        executor.submit(createFactoryTask());
        factions.forEach(executor::submit);

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
            executor.shutdownNow();
        }

        factions.forEach(f ->
                System.out.println(f.getName() + " robots: " + f.getCompleteRobotCount())
        );
    }

    private Runnable createFactoryTask() {
        return () -> {
            for (int day = 0; day < SimulationConfig.DAYS; day++) {
                factory.produceParts(SimulationConfig.FACTORY_PARTS_PER_DAY);
            }
        };
    }
}
