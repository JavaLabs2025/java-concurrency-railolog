package org.labs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Utils {
    private static final Random RANDOM = new Random();

    public static int discussTime() {
        return random(10, 20);
    }

    public static int eatTime() {
        return random(20, 40);
    }

    public static int random(int min, int max) {
        return RANDOM.nextInt(min, max);
    }

    public static Map<Integer, Double> calculatePercentiles(List<Integer> data, int... percentiles) {
        List<Integer> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);

        Map<Integer, Double> results = new LinkedHashMap<>();
        for (int p : percentiles) {
            results.put(p, calculatePercentile(sortedData, p));
        }
        return results;
    }

    private static double calculatePercentile(List<Integer> sortedList, int percentile) {
        if (sortedList == null || sortedList.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        if (percentile < 0 || percentile > 100) {
            throw new IllegalArgumentException("Percentile must be between 0 and 100");
        }

        int n = sortedList.size();

        if (percentile == 0) return sortedList.getFirst();
        if (percentile == 100) return sortedList.get(n - 1);

        double rank = percentile / 100.0 * (n - 1);
        int lowerIndex = (int) Math.floor(rank);
        int upperIndex = (int) Math.ceil(rank);

        if (lowerIndex == upperIndex) {
            return sortedList.get(lowerIndex);
        }

        double lowerValue = sortedList.get(lowerIndex);
        double upperValue = sortedList.get(upperIndex);
        double fraction = rank - lowerIndex;

        return lowerValue + fraction * (upperValue - lowerValue);
    }
}
