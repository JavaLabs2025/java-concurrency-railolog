package org.labs;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.labs.hierarchy.DinnerFactory;
import org.labs.hierarchy.DinnerResult;

@Slf4j
public class Main {

    public static final int[] PERCENTILES = {50, 90, 95, 100};

    @SneakyThrows
    public static void main(String[] args) {
        int programmersCount;
        int waitersCount;
        int servings;
        programmersCount = 2;
        waitersCount = 1;
        servings = 300;

        dinnerSample(programmersCount, waitersCount, servings);

        programmersCount = 20;
        dinnerSample(programmersCount, waitersCount, servings);

        programmersCount = 200;
        dinnerSample(programmersCount, waitersCount, servings);
//
        programmersCount = 10_000;
        waitersCount = 1;
        servings = 600_000;

        dinnerSample(programmersCount, waitersCount, servings);

        waitersCount = 10;
        dinnerSample(programmersCount, waitersCount, servings);

        waitersCount = 1000;
        dinnerSample(programmersCount, waitersCount, servings);

        programmersCount = 10_000;
        waitersCount = 1000;
        servings = 1_000_000;

        dinnerSample(programmersCount, waitersCount, servings);

        programmersCount = 20_000;
        dinnerSample(programmersCount, waitersCount, servings);

        programmersCount = 200_000;
        dinnerSample(programmersCount, waitersCount, servings);
    }

    private static void dinnerSample(int programmersCount, int waitersCount, int servings) {
        DinnerFactory dinnerFactory = new DinnerFactory(
                programmersCount,
                waitersCount,
                servings,
                Executors.newVirtualThreadPerTaskExecutor(),
                Executors.newVirtualThreadPerTaskExecutor()
        );

        DinnerResult dinnerResult = dinnerFactory.setupAndRun();
        logResults(dinnerResult);
    }


    private static void logResults(DinnerResult dinnerResult) {
        log.info("Each programmer had servings by percentiles: \n{}", printPercentiles(Utils.calculatePercentiles(
                dinnerResult.servingsEaten(),
                PERCENTILES
        )));
    }

    private static String printPercentiles(Map<Integer, Double> percentilesTable) {
        return percentilesTable.entrySet().stream()
                .map(e -> String.format("  p%d: %10.2f", e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));

    }
}
