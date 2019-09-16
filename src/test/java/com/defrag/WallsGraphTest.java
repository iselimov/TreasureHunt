package com.defrag;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WallsGraphTest {

    /**
     * Input data:
     0
     50 50
     */
    @Test
    public void testSeekDoorsAmount_When_ThereIsNotExteriorWalls() {
        assertEquals(1, Main.doorsAmount(createAxesLines(), new Point(50, 50)));
    }

    /**
     * Input data:
     2
     0 10 90 100
     10 100 90 0
     90 50
     */
    @Test
    public void testSeekDoorsAmount_When_ExteriorWallsExist1() {
        List<Line.Builder> lines = createAxesLines();

        // y = x + 10
        lines.add(createLine(new Point(0, 10), new Point(90, 100)));
        // y = -1.25x + 112.5
        lines.add(createLine(new Point(10, 100), new Point(90, 0)));

        assertEquals(1, Main.doorsAmount(lines, new Point(90, 50)));
        assertEquals(1, Main.doorsAmount(lines, new Point(10, 10)));
    }

    /**
     * Input data:
     4
     30 0 30 100
     70 0 70 100
     0 30 100 30
     0 70 100 70
     50 50
     */
    @Test
    public void testSeekDoorsAmount_When_ExteriorWallsExist2() {
        List<Line.Builder> lines = createAxesLines();

        // x = 30
        lines.add(createLine(new Point(30, 0), new Point(30, 100)));
        // x = 70
        lines.add(createLine(new Point(70, 0), new Point(70, 100)));

        // y = 30
        lines.add(createLine(new Point(0, 30), new Point(100, 30)));
        // y = 70
        lines.add(createLine(new Point(0, 70), new Point(100, 70)));

        assertEquals(2, Main.doorsAmount(lines, new Point(50, 50)));
        assertEquals(1, Main.doorsAmount(lines, new Point(95.5, 94.5)));
        assertEquals(1, Main.doorsAmount(lines, new Point(1.3, 1.8)));
    }

    /**
     * Input data:
     11
     10 0 10 100
     20 0 20 100
     30 0 30 100
     70 0 70 100
     80 0 80 100
     0 10 100 10
     0 20 100 20
     0 30 100 30
     0 70 100 70
     0 80 100 80
     0 90 100 90
     50 50
     */
    @Test
    public void testSeekDoorsAmount_When_ExteriorWallsExist3() {
        List<Line.Builder> lines = createAxesLines();

        // x = 10
        lines.add(createLine(new Point(10, 0), new Point(10, 100)));
        // x = 20
        lines.add(createLine(new Point(20, 0), new Point(20, 100)));
        // x = 30
        lines.add(createLine(new Point(30, 0), new Point(30, 100)));
        // x = 70
        lines.add(createLine(new Point(70, 0), new Point(70, 100)));
        // x = 80
        lines.add(createLine(new Point(80, 0), new Point(80, 100)));

        // y = 10
        lines.add(createLine(new Point(0, 10), new Point(100, 10)));
        // y = 20
        lines.add(createLine(new Point(0, 20), new Point(100, 20)));
        // y = 30
        lines.add(createLine(new Point(0, 30), new Point(100, 30)));
        // y = 70
        lines.add(createLine(new Point(0, 70), new Point(100, 70)));
        // y = 80
        lines.add(createLine(new Point(0, 80), new Point(100, 80)));
        // y = 90
        lines.add(createLine(new Point(0, 90), new Point(100, 90)));

        assertEquals(3, Main.doorsAmount(lines, new Point(50, 50)));
        assertEquals(1, Main.doorsAmount(lines, new Point(9, 9)));
        assertEquals(1, Main.doorsAmount(lines, new Point(85, 85)));
        assertEquals(2, Main.doorsAmount(lines, new Point(15, 15)));
    }

    /**
     * Input data:
     7
     20 0 37 100
     40 0 76 100
     85 0 0 75
     100 90 0 90
     0 71 100 61
     0 14 100 38
     100 47 47 100
     54.5 55.4
     */
    @Test
    public void testSeekDoorsAmount_When_ExteriorWallsExist4() {
        List<Line.Builder> lines = createAxesLines();

        lines.add(createLine(new Point(20, 0), new Point(37, 100)));
        lines.add(createLine(new Point(40, 0), new Point(76, 100)));
        lines.add(createLine(new Point(85, 0), new Point(0, 75)));
        lines.add(createLine(new Point(100, 90), new Point(0, 90)));
        lines.add(createLine(new Point(0, 71), new Point(100, 61)));
        lines.add(createLine(new Point(0, 14), new Point(100, 38)));
        lines.add(createLine(new Point(100, 47), new Point(47, 100)));

        assertEquals(2, Main.doorsAmount(lines, new Point(54.5, 55.4)));
        assertEquals(1, Main.doorsAmount(lines, new Point(10, 95)));
        assertEquals(2, Main.doorsAmount(lines, new Point(75, 71)));
        assertEquals(1, Main.doorsAmount(lines, new Point(2, 72)));
        assertEquals(1, Main.doorsAmount(lines, new Point(2, 72)));
        assertEquals(2, Main.doorsAmount(lines, new Point(20, 61)));
    }

    private List<Line.Builder> createAxesLines() {
        return new ArrayList<>(Arrays.asList(
                // y = 0
                createLine(new Point(0, 0), new Point(100, 0)),
                // x = 100
                createLine(new Point(100, 0), new Point(100, 100)),
                // y = 100
                createLine(new Point(100, 100), new Point(0, 100)),
                // x = 0
                createLine(new Point(0, 100), new Point(0, 0))
        ));
    }

    private Line.Builder createLine(Point first, Point second) {
        return new Line.Builder(first, second);
    }
}
