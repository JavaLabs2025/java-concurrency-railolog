package org.labs;

import java.util.List;
import java.util.Random;

public class Utils {
    private static final Random RANDOM = new Random();

    public static int discussTime() {
        return random(1, 2);
    }

    public static int eatTime() {
        return random(2, 4);
    }

    public static int random(int min, int max) {
        return RANDOM.nextInt(min, max);
    }

    public static int findMedian(List<Integer> numbers) {
        int size = numbers.size();
        if (size % 2 == 1) {
            return numbers.get(size / 2);
        } else {
            int mid1 = numbers.get(size / 2 - 1);
            int mid2 = numbers.get(size / 2);
            return (mid1 + mid2) / 2;
        }
    }
}
