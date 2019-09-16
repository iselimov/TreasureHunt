package com.defrag;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LineTest {

    @Test
    public void testGetIntersectionPoint_When_CoefficientsKAreEqual() {
        // y = x
        Line.Builder first = new Line.Builder(
                new Point(1, 1), new Point(0, 0)
        );

        // y = x + 2
        Line.Builder second = new Line.Builder(
                new Point(1, 3), new Point(0, 2)
        );

        assertEquals(Optional.empty(), first.getIntersectedPoint(second));
    }

    @Test
    public void testGetIntersectionPoint_When_CoefficientsKAreNotEqual() {
        // y = 0
        Line.Builder first = new Line.Builder(
                new Point(1, 0), new Point(2, 0)
        );

        // x = 0
        Line.Builder second = new Line.Builder(
                new Point(0, 10), new Point(0, 20)
        );

        assertEquals(Optional.of(new Point(0, 0)), first.getIntersectedPoint(second));
    }

    @Test
    public void testGetIntersectionPoint_When_IntersectionPointIsOutOfBorder() {
        // y = x + 100
        Line.Builder first = new Line.Builder(
                new Point(0, 100), new Point(1, 101)
        );

        // x = 100
        Line.Builder second = new Line.Builder(
                new Point(100, 10), new Point(100, 20)
        );

        // (100, 200)
        assertEquals(Optional.empty(), first.getIntersectedPoint(second));
    }
}
