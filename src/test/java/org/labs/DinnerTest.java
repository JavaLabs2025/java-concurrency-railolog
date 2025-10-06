package org.labs;

import java.util.Collections;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.labs.hierarchy.DinnerFactory;
import org.labs.hierarchy.DinnerResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DinnerTest {

    @Test
    void dinnerForOne_allServingsEaten() {
        int programmersCount = 1;
        int waitersCount = 1;
        int servings = 20;
        DinnerFactory dinnerFactory = new DinnerFactory(
                programmersCount,
                waitersCount,
                servings,
                Executors.newVirtualThreadPerTaskExecutor(),
                Executors.newVirtualThreadPerTaskExecutor()
        );
        DinnerResult dinnerResult = dinnerFactory.setupAndRun();

        assertEquals(0, dinnerResult.servingsLeft());
    }

    @Test
    void dinner_manyWaiters_allServingsEaten() {
        int programmersCount = 2;
        int waitersCount = 100;
        int servings = 20;
        DinnerFactory dinnerFactory = new DinnerFactory(
                programmersCount,
                waitersCount,
                servings,
                Executors.newVirtualThreadPerTaskExecutor(),
                Executors.newVirtualThreadPerTaskExecutor()
        );
        DinnerResult dinnerResult = dinnerFactory.setupAndRun();

        assertEquals(0, dinnerResult.servingsLeft());
    }

    @Test
    void dinner_fairlyDistributed() {
        int programmersCount = 20;
        int waitersCount = 5;
        int servings = 1000;
        DinnerFactory dinnerFactory = new DinnerFactory(
                programmersCount,
                waitersCount,
                servings,
                Executors.newVirtualThreadPerTaskExecutor(),
                Executors.newVirtualThreadPerTaskExecutor()
        );
        DinnerResult dinnerResult = dinnerFactory.setupAndRun();

        assertEquals(0, dinnerResult.servingsLeft());
        assertTrue(
                Collections.min(dinnerResult.servingsEaten()) * 2 >= Collections.max(dinnerResult.servingsEaten())
        );
    }
}
