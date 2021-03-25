package uk.co.gosseyn.xanax.domain;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.*;
class PointTest {

    @Test
    public void distanceTo() {
        float distance = new Point(1,2,3)
                .distanceToPow2(new Point(123, 345, 567));
        assertEquals(67128904, (int)(distance * 100000));
    }

    @Test
    public void distanceToTime() {
        // Works out about 20% faster without sqrt
        long start = System.nanoTime();
        range(0, 100000000).forEach(i -> {
            randomPoint().distanceTo(randomPoint());
        });
        System.out.println((System.nanoTime() - start) / 1000000);
        start = System.nanoTime();
        range(0, 100000000).forEach(i -> {
            randomPoint().distanceToPow2(randomPoint());
        });
        System.out.println((System.nanoTime() - start) / 1000000);
    }

    private Point randomPoint() {
        // TODO random here is very expensive.
        return new Point((int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
    }
}