package org.labs.hierarchy;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class Spoon {
    private final int number;
    private final Lock lock = new ReentrantLock();
}
