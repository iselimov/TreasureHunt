package com.defrag;

import java.util.List;

/**
 * Entry point.
 *
 * Some test samples can be found in com.defrag.WallsGraphTest to check the whole process
 * except the parsing stage.
 * Test sample from the task is there as well.
 *
 * @author Ilyas Selimov iselimov92@gmail.com
 */
public class Main {

    public static void main(String[] args) {
        InputDataParser inputDataParser = new InputDataParser();
        inputDataParser.parse();

        int result = doorsAmount(inputDataParser.getLineSources(), inputDataParser.getTreasurePoint());
        System.out.println(String.format("Number of doors = %d", result));
    }

    static int doorsAmount(List<Line.Builder> lineSources,
                           Point treasurePoint) {
        List<Line> preparedLines = new LinesCreator(lineSources)
                .create();

        WallsGraph.Builder graphBuilder = new WallsGraph.Builder();
        for (Line preparedLine : preparedLines) {
            for (Wall wall : preparedLine.buildWalls()) {
                graphBuilder.addEdgeForWall(wall);
            }
        }

        return graphBuilder.build().seekDoorsAmount(treasurePoint);
    }
}

