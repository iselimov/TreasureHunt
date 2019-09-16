package com.defrag;

import com.defrag.struct.DisjointSet;

import java.util.*;
import java.util.stream.Collectors;

class WallsGraph {

    /**
     * Representation of adjacent list of the graph.
     */
    private final Map<Point, List<Wall>> adjacentList;
    /**
     * Helps to union the walls in the rooms and to seek an exit at further
     */
    private final DisjointSet<Wall> roomsSet = new DisjointSet<>();

    private Wall treasureRoomPresenter;

    private WallsGraph(Builder builder) {
        adjacentList = builder.adjacentList;
    }

    int seekDoorsAmount(Point treasurePoint) {
        buildRooms(treasurePoint);

        if (treasureRoomPresenter == null) {
            throw new IllegalStateException("Couldn't find room with treasure");
        }

        Wall currWall = treasureRoomPresenter;

        int doorsCounter = 1;
        int lastNonVisitedWallCounter = -1;
        int currNonVisitedWallCounter = -1;

        Queue<Wall> nonVisitedWalls = new LinkedList<>();
        while (true) {
            List<Wall> roomWithCurrWall = roomsSet.elementsInTheSameSet(currWall);
            if (lastNonVisitedWallCounter == -1) {
                lastNonVisitedWallCounter = roomWithCurrWall.size();
                currNonVisitedWallCounter = lastNonVisitedWallCounter;
            }

            // we have to union these sets because we don't need a duplicate traversal through visited walls
            roomsSet.union(treasureRoomPresenter, currWall);

            for (Wall wall : roomWithCurrWall) {
                if (wall.isExterior()) {
                    if (currNonVisitedWallCounter != lastNonVisitedWallCounter) {
                        doorsCounter++;
                    }

                    return doorsCounter;
                }

                Wall wallOfAdjacentRoom = wall.createReverse();
                if (roomsSet.find(wallOfAdjacentRoom) != roomsSet.find(treasureRoomPresenter)) {
                    nonVisitedWalls.add(wallOfAdjacentRoom);
                }
            }

            if (nonVisitedWalls.isEmpty()) {
                throw new IllegalStateException("Couldn't find an exit");
            }

            currWall = nonVisitedWalls.peek();

            currNonVisitedWallCounter--;
            if (currNonVisitedWallCounter == 0) {
                lastNonVisitedWallCounter = nonVisitedWalls.size();
                currNonVisitedWallCounter = nonVisitedWalls.size();

                doorsCounter++;
            }

            nonVisitedWalls.poll();
        }
    }

    private void buildRooms(Point treasurePoint) {
        List<Wall> allWalls = adjacentList.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Set<Wall> visitedWalls = new HashSet<>();

        for (Wall wall : allWalls) {
            // we start traversing only with CO_DIRECTED walls,
            // anyway NOT_CO_DIRECTED ones will be traversed as well
            if (wall.isNotCoDirected() || !visitedWalls.add(wall)) {
                continue;
            }

            roomsSet.make(wall);

            boolean isTreasureRoom = true;

            Wall nextWall = wall;
            while (true) {
                WallSeeker seeker = new WallSeeker(nextWall);

                // we try to define right away whether it's a treasure in a certain room
                isTreasureRoom &= seeker.isSuitableAdjacentWall(treasurePoint);

                nextWall = seeker.seek(adjacentList.get(nextWall.getEnd()))
                        .orElseThrow(() -> new IllegalStateException("Couldn't find next wall"));

                // we need to create rooms
                roomsSet.make(nextWall);
                roomsSet.union(wall, nextWall);

                visitedWalls.add(nextWall);

                if (nextWall.getEnd().equals(wall.getStart())) {
                    // we need another one treasure seeking operation before exit
                    isTreasureRoom &= new WallSeeker(nextWall).isSuitableAdjacentWall(treasurePoint);

                    break;
                }
            }

            if (isTreasureRoom) {
                // we prepare a presenter for the next step, that what we need to start from
                treasureRoomPresenter = roomsSet.find(wall);
            }
        }
    }

    static class Builder {

        private final Map<Point, List<Wall>> adjacentList = new HashMap<>();

        /**
         * Create edges in the graph.
         * Our "border lines" have the only edge in a certain direction.
         * Other lines have both of the directions.
         */
        Builder addEdgeForWall(Wall wall) {
            Line line = wall.getLine();

            if (line.isBottomBorder() || line.isRightBorder()) {
                addEdge(wall);
            } else if (line.isTopBorder() || line.isLeftBorder()) {
                addEdge(wall.createReverse());
            } else {
                addEdge(wall);
                addEdge(wall.createReverse());
            }

            return this;
        }

        WallsGraph build() {
            return new WallsGraph(this);
        }

        private void addEdge(Wall wall) {
            List<Wall> adjacentToStartPoint = this.adjacentList.computeIfAbsent(wall.getStart(),
                    k -> new ArrayList<>());
            adjacentToStartPoint.add(wall);
        }
    }
}
