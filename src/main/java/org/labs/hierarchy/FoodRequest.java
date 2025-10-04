package org.labs.hierarchy;

import java.util.concurrent.CountDownLatch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public final class FoodRequest {
    private final int clientId;
    private final CountDownLatch latch = new CountDownLatch(1);
    private boolean isServed = false;

    @SneakyThrows
    public boolean getServed() {
        latch.await();
        return isServed;
    }

    public void setServed() {
        isServed = true;
        latch.countDown();
    }

    public void setUnserved() {
        isServed = false;
        latch.countDown();
    }
}
