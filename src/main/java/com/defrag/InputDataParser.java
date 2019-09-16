package com.defrag;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class InputDataParser {
    /**
     * Sets up size of axes.
     */
    static final Point BOTTOM_LEFT_POINT = new Point(0, 0);
    static final Point TOP_RIGHT_POINT = new Point(100, 100);

    private static final int COORDINATE_AXIS_AMOUNT = 4;

    private final Scanner scanner = new Scanner(System.in);

    private List<Line.Builder> lineSources = new ArrayList<>();
    private Point treasurePoint;

    void parse() {
        parseLineSources();
        parseTreasurePoint();
    }

    List<Line.Builder> getLineSources() {
        return lineSources;
    }

    Point getTreasurePoint() {
        return treasurePoint;
    }

    private void parseLineSources() {
        int numberOfWalls;
        if (scanner.hasNextInt()) {
            numberOfWalls = scanner.nextInt() + COORDINATE_AXIS_AMOUNT;
        } else {
            throw new IllegalArgumentException("Number of interior walls wasn't parsed correctly");
        }

        fillAxisLines();
        fillInputLines(numberOfWalls);
    }

    private void fillAxisLines() {
        lineSources.add(
                new Line.Builder(
                        new Point(BOTTOM_LEFT_POINT.getX(), BOTTOM_LEFT_POINT.getY()),
                        new Point(TOP_RIGHT_POINT.getX(), BOTTOM_LEFT_POINT.getY()))
        );
        lineSources.add(
                new Line.Builder(
                        new Point(TOP_RIGHT_POINT.getX(), BOTTOM_LEFT_POINT.getY()),
                        new Point(TOP_RIGHT_POINT.getX(), TOP_RIGHT_POINT.getY()))
        );
        lineSources.add(
                new Line.Builder(
                        new Point(TOP_RIGHT_POINT.getX(), TOP_RIGHT_POINT.getY()),
                        new Point(BOTTOM_LEFT_POINT.getX(), TOP_RIGHT_POINT.getY()))
        );
        lineSources.add(
                new Line.Builder(
                        new Point(BOTTOM_LEFT_POINT.getX(), TOP_RIGHT_POINT.getY()),
                        new Point(BOTTOM_LEFT_POINT.getX(), BOTTOM_LEFT_POINT.getY()))
        );
    }

    private void fillInputLines(int numberOfWalls) {
        for (int i = COORDINATE_AXIS_AMOUNT; i < numberOfWalls; i++) {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Lines coordinate wasn't parsed entirely");
            }

            int startX = scanner.nextInt();
            int startY = scanner.nextInt();

            int endX = scanner.nextInt();
            int endY = scanner.nextInt();

            lineSources.add(
                    new Line.Builder(
                            new Point(startX, startY), new Point(endX, endY))
            );
        }
    }

    private void parseTreasurePoint() {
        Double treasurePointX = null;
        if (scanner.hasNextDouble()) {
            treasurePointX = scanner.nextDouble();
        }

        Double treasurePointY = null;
        if (scanner.hasNextDouble()) {
            treasurePointY = scanner.nextDouble();
        }

        if (treasurePointX == null || treasurePointY == null) {
            throw new IllegalArgumentException("Treasure point wasn't parsed correctly");
        }

        treasurePoint = new Point(treasurePointX, treasurePointY);
    }
}
