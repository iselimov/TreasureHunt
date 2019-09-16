package com.defrag;

import java.util.ArrayList;
import java.util.List;

class LinesCreator {

    private final List<Line.Builder> lineSources;

    LinesCreator(List<Line.Builder> lineSources) {
        this.lineSources = lineSources;
    }

    /**
     * @return list of lines with found intersected points belonging to them.
     */
    List<Line> create() {
        List<Line> result = new ArrayList<>();

        int numberOfWalls = lineSources.size();

        for (int i = 0; i < numberOfWalls; i++) {
            Line.Builder currLine = lineSources.get(i);
            for (int j = i + 1; j < numberOfWalls; j++) {
                Line.Builder otherLine = lineSources.get(j);

                currLine.getIntersectedPoint(otherLine).ifPresent(p -> {
                    currLine.addBelongingPoint(p);
                    otherLine.addBelongingPoint(p);
                });
            }

            result.add(currLine.build());
        }

        return result;
    }
}
