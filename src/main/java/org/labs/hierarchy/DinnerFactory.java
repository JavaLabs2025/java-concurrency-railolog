package org.labs.hierarchy;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.labs.Utils;

@Slf4j
@RequiredArgsConstructor
public class DinnerFactory {

    private final int programmersCount;
    private final int waitersCount;
    private final ExecutorService programmersPool;
    private final ExecutorService waitersPool;

    @SneakyThrows
    public DinnerSummary setupAndRun() {
        List<Spoon> spoons = createSpoons();
        Restaurant restaurant = new Restaurant();
        List<Programmer> programmers = createProgrammers(restaurant, spoons);
        List<Waiter> waiters = createWaiters(restaurant);

        programmers.forEach(programmersPool::submit);
        waiters.forEach(waitersPool::submit);

        monitorRestaurant(restaurant);

        programmersPool.shutdown();
        waitersPool.shutdown();
        try {
            if (!programmersPool.awaitTermination(10, TimeUnit.SECONDS)) {
                programmersPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            programmersPool.shutdownNow();
        }
//        programmers.forEach(p -> log.info("Programmer {} eat total {} servings", p.getId(), p.getTotalServings()));
        try {
            if (!waitersPool.awaitTermination(2, TimeUnit.SECONDS)) {
                waitersPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            waitersPool.shutdownNow();
        }

        Integer max = programmers.stream()
                .map(Programmer::getTotalServings)
                .max(Integer::compareTo)
                .get();
        Integer min = programmers.stream()
                .map(Programmer::getTotalServings)
                .min(Integer::compareTo)
                .get();

        return new DinnerSummary(
                min,
                max,
                Utils.findMedian(programmers.stream().map(Programmer::getTotalServings).sorted().toList())
        );
    }

    @SneakyThrows
    private void monitorRestaurant(Restaurant restaurant) {
        while (restaurant.isFoodAvailable()) {
            Thread.sleep(500);
            log.info("Restaurant have {} servings left", restaurant.getFoodServings());
        }
    }

    private List<Spoon> createSpoons() {
        return IntStream.range(0, programmersCount)
                .mapToObj(Spoon::new)
                .toList();
    }

    private List<Programmer> createProgrammers(Restaurant restaurant, List<Spoon> spoons) {
        return IntStream.range(0, programmersCount)
                .mapToObj(i -> new Programmer(i, spoons.get(i), spoons.get((i + 1) % programmersCount), restaurant))
                .toList();
    }

    private List<Waiter> createWaiters(Restaurant restaurant) {
        return IntStream.range(0, waitersCount)
                .mapToObj(i -> new Waiter(restaurant))
                .toList();
    }
}
