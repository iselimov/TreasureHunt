package com.defrag;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class WallSeekerTest {

    @Test
    public void testSeek_When_AdjacentWallsIsEmpty() {
        // it doesn't matter whether current wall is null or not
        assertEquals(Optional.empty(), new WallSeeker(null).seek(Collections.emptyList()));
    }

    @Test
    public void testSeek_When_AdjacentWallsIsNotEmptyAndCurrentWallIsCoDirected() {
        // y= x + 10
        Line firstLine = new Line.Builder(
                new Point(0, 10), new Point(90, 100)
        ).build();
        // y = -1.25x + 112.5
        Line secondLine = new Line.Builder(
                new Point(10, 100), new Point(90, 0)
        ).build();

        Point intersectionPoint = new Point(45, 55);

        Wall currWall = new Wall(firstLine, new Point(0, 10), intersectionPoint);

        Wall firstAdjacent = new Wall(firstLine, intersectionPoint, new Point(90, 100));
        Wall secondAdjacent = new Wall(firstLine, intersectionPoint, new Point(0, 10));
        Wall thirdAdjacent = new Wall(secondLine, intersectionPoint, new Point(10, 100));
        Wall fourthAdjacent = new Wall(secondLine, intersectionPoint, new Point(90, 0));
        // same to currWall but with opposite direction
        List<Wall> adjacentWalls = Arrays.asList(
                firstAdjacent,
                secondAdjacent,
                thirdAdjacent,
                fourthAdjacent
        );

        Optional<Wall> result = new WallSeeker(currWall).seek(adjacentWalls);
        assertEquals(Optional.of(thirdAdjacent), result);
    }

    @Test
    public void testSeek_When_AdjacentWallsIsNotEmptyAndCurrentWallIsNotCoDirected() {
        // y = -1.25x + 112.5
        Line firstLine = new Line.Builder(
                new Point(10, 100), new Point(90, 0)
        ).build();

        // y = 100
        Line secondLine = new Line.Builder(
                new Point(0, 100), new Point(10, 100)
        ).build();

        Wall currWall = new Wall(firstLine, new Point(45, 55), new Point(10, 100));

        // same to currWall but in opposite direction
        Wall firstAdjacent = new Wall(firstLine, new Point(10, 100), new Point(45, 55));
        Wall secondAdjacent = new Wall(secondLine, new Point(10, 100), new Point(0, 100));

        List<Wall> adjacentWalls = Arrays.asList(
                firstAdjacent,
                secondAdjacent
        );

        Optional<Wall> result = new WallSeeker(currWall).seek(adjacentWalls);
        assertEquals(Optional.of(secondAdjacent), result);
    }
}
