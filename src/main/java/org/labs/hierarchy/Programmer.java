package org.labs.hierarchy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.labs.Utils;

@Slf4j
@RequiredArgsConstructor
public class Programmer implements Runnable {

    @Getter
    private final int id;
    private final Spoon leftSpoon;
    private final Spoon rightSpoon;
    private final Restaurant restaurant;

    @Getter
    private int totalServings = 0;
    private State state = State.DISCUSSING;
    private volatile boolean isFinished = false;


    @Override
    public void run() {
        while (!isFinished) {
            switch (state) {
                case DISCUSSING -> discuss();
                case HUNGRY -> requestFood();
                case EATING -> eat();
            }
        }
//        log.info("Programmer {} leaving execution method", id);
    }

    @SneakyThrows
    private void eat() {
        takeSpoons();
        int eatTimeMs = Utils.eatTime();
//        log.info("Programmer {} is eating for {} ms", id, eatTimeMs);
        Thread.sleep(eatTimeMs);
//        log.info("Programmer {} finished its meal", id);
        releaseSpoons();

        state = State.DISCUSSING;
        totalServings++;
    }

    private void takeSpoons() {
        if (leftSpoon.getNumber() < rightSpoon.getNumber()) {
            leftSpoon.getLock().lock();
            rightSpoon.getLock().lock();
        } else {
            rightSpoon.getLock().lock();
            leftSpoon.getLock().lock();
        }
    }

    private void releaseSpoons() {
        leftSpoon.getLock().unlock();
        rightSpoon.getLock().unlock();
    }

    @SneakyThrows
    private void discuss() {
        int discussTimeMs = Utils.discussTime();
//        log.info("Programmer {} is discussing for {} ms", id, discussTimeMs);
        Thread.sleep(discussTimeMs);
//        log.info("Programmer {} finished discussing", id);

        state = State.HUNGRY;
    }

    @SneakyThrows
    private void requestFood() {
        FoodRequest request = restaurant.requestFood(id, totalServings);
//        log.info("Programmer {} is waiting for food", id);
        boolean served = request.getServed();

        if (!served) {
            isFinished = true;
//            log.info("The dinner is over, programmer is shutting down");
        } else {
//            log.info("Programmer {} received food", id);
            receiveFood();
        }
    }

    private void receiveFood() {
        state = State.EATING;
    }
}