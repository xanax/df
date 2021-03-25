package uk.co.gosseyn.xanax.domain;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

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
        long start = System.nanoTime();
        IntStream.range(0, 100000000).forEach(i -> {
            randomPoint().distanceToPow2(randomPoint());
        });
        System.out.println((System.nanoTime() - start) / 1000000);
    }

    private Point randomPoint() {
        return new Point((int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100));
    }
}