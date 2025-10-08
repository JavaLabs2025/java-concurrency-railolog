package org.labs.hierarchy;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;

public class Restaurant {
    private final AtomicInteger foodServings;
    private final PriorityBlockingQueue<FoodRequest> requests = new PriorityBlockingQueue<>();

    public Restaurant(
            int servingsCount
    ) {
        this.foodServings = new AtomicInteger(servingsCount);
    }

    @SneakyThrows
    public FoodRequest requestFood(int clientId, int alreadyEaten) {
        FoodRequest request = new FoodRequest(clientId, alreadyEaten);
        requests.put(request);
        return request;
    }

    @SneakyThrows
    public FoodRequest getNextRequest() {
        return requests.poll(1, TimeUnit.SECONDS);
    }

    public boolean isFoodAvailable() {
        return foodServings.get() > 0;
    }

    public boolean getFood() {
        int servings = foodServings.get();
        while (servings > 0) {
            boolean success = foodServings.compareAndSet(servings, servings - 1);
            if (success) {
                return true;
            } else {
                servings = foodServings.get();
            }
        }
        return false;
    }

    public int getFoodServings() {
        return foodServings.get();
    }
}
