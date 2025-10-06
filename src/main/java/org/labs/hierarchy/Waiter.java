package org.labs.hierarchy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Waiter implements Runnable {

    private final Restaurant restaurant;

    private volatile boolean isFinished = false;

    @Override
    public void run() {
        while (!isFinished) {
            FoodRequest request = restaurant.getNextRequest();
//            log.info("Waiter is serving request: {}", request);
            if (request != null) {
                boolean food = restaurant.getFood();
                if (food) {
                    request.setServed();
                } else {
                    request.setUnserved();
                }
            } else {
//                log.info("Waiter got no new request, checking if food is available");
                if (!restaurant.isFoodAvailable()) {
//                    log.info("Waiter is finishing");
                    isFinished = true;
                }
            }
        }
//        log.info("Waiter leaving execution method");
    }
}
