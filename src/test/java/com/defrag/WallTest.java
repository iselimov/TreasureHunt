package com.defrag;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WallTest {

    @Test
    public void testNewInstance_When_LineIsExterior() {
        // y = 0
        Line line = new Line.Builder(
                new Point(0, 0), new Point(20, 0)
        ).build();

        Wall wall = new Wall(line, new Point(0, 0), new Point(20, 0));

        assertTrue(wall.isExterior());
    }

    @Test
    public void testNewInstance_When_LineIsNotExterior() {
        // y = x
        Line line = new Line.Builder(
                new Point(0, 0), new Point(1, 1)
        ).build();

        Wall wall = new Wall(line, new Point(1, 1), new Point(2, 2));

        assertFalse(wall.isExterior());
    }

    @Test
    public void testNewInstance_When_DirectionTypeShouldBeCoDirected() {
        // y = x
        Line line = new Line.Builder(
                new Point(0, 0), new Point(1, 1)
        ).build();

        Wall wall = new Wall(line, new Point(1, 1), new Point(2, 2));

        assertFalse(wall.isNotCoDirected());
    }

    @Test
    public void testNewInstance_When_DirectionTypeShouldBeNotCoDirected() {
        // y = -x
        Line line = new Line.Builder(
                new Point(0, 0), new Point(-1, 1)
        ).build();

        // k is negative and y1 less than y2
        Wall wall = new Wall(line, new Point(-1, 1), new Point(-2, 2));

        assertTrue(wall.isNotCoDirected());
    }
}