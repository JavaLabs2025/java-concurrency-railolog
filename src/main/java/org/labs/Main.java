package org.labs;

import java.util.concurrent.Executors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.labs.hierarchy.DinnerFactory;
import org.labs.hierarchy.DinnerSummary;

@Slf4j
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        int programmersCount = 1_000;
        int waitersCount = 10;

        DinnerFactory dinnerFactory = new DinnerFactory(
                programmersCount,
                waitersCount,
                Executors.newVirtualThreadPerTaskExecutor(),
                Executors.newVirtualThreadPerTaskExecutor()
        );

        DinnerSummary dinnerSummary = dinnerFactory.setupAndRun();
        log.info("Dinner summary: {}", dinnerSummary);
    }
}
