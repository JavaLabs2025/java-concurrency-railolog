package org.labs.hierarchy;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;

public class Restaurant {
    private final AtomicInteger foodServings = new AtomicInteger(1_000_000);
    private final LinkedBlockingQueue<FoodRequest> requests = new LinkedBlockingQueue<>();

    @SneakyThrows
    public FoodRequest requestFood(int clientId) {
        FoodRequest request = new FoodRequest(clientId);
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
