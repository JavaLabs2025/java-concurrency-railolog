package org.labs.hierarchy;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DinnerFactory {

    private final int programmersCount;
    private final int waitersCount;
    private final int servingsCount;
    private final ExecutorService programmersPool;
    private final ExecutorService waitersPool;

    @SneakyThrows
    public DinnerResult setupAndRun() {
        log.info(
                "Starting dinner with \n{} programmers \n{} waiters \n{} servings",
                programmersCount,
                waitersCount,
                servingsCount
        );

        List<Spoon> spoons = createSpoons();
        Restaurant restaurant = new Restaurant(servingsCount);
        List<Programmer> programmers = createProgrammers(restaurant, spoons);
        List<Waiter> waiters = createWaiters(restaurant);

        long startTime = (long) (System.nanoTime() / 1e6);

        programmers.forEach(programmersPool::submit);
        waiters.forEach(waitersPool::submit);

        // blocking current thread
        monitorRestaurant(restaurant);

        long finishTime = (long) (System.nanoTime() / 1e6);

        programmersPool.shutdown();
        waitersPool.shutdown();
        try {
            if (!programmersPool.awaitTermination(2, TimeUnit.SECONDS)) {
                programmersPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            programmersPool.shutdownNow();
        }

        try {
            if (!waitersPool.awaitTermination(2, TimeUnit.SECONDS)) {
                waitersPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            waitersPool.shutdownNow();
        }

        log.info("All servings were eaten in {} ms", finishTime - startTime);
        return new DinnerResult(
                programmers.stream()
                        .map(Programmer::getTotalServings)
                        .collect(Collectors.toList()),
                restaurant.getFoodServings()
        );
    }

    @SneakyThrows
    private void monitorRestaurant(Restaurant restaurant) {
        while (restaurant.isFoodAvailable()) {
            Thread.sleep(500);
//            log.info("Restaurant have {} servings left", restaurant.getFoodServings());
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
