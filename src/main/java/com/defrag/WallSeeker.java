package com.defrag;

import java.util.List;
import java.util.Optional;

/**
 * Seek the next suitable point to create a room.
 */
class WallSeeker {

    private final Wall currentWall;

    WallSeeker(Wall currentWall) {
        this.currentWall = currentWall;
    }

    /**
     * The algo includes transforming coordinates in the new axes.
     * and checking found point according by {@link Wall#directionType} characteristic.
     *
     * @param adjacentWalls list of adjacent walls to currentWall
     * @return {@link Optional#empty()} if result wasn't found.
     */
    Optional<Wall> seek(List<Wall> adjacentWalls) {
        if (adjacentWalls.isEmpty()) {
            return Optional.empty();
        }

        for (Wall adjacentWall : adjacentWalls) {
            if (currentWall.getLine().equals(adjacentWall.getLine())) {
                continue;
            }
            if (currentWall.hasOppositeDirectionTo(adjacentWall)) {
                continue;
            }

            boolean isSuitableAdjacentWall = isSuitableAdjacentWall(adjacentWall.getEnd());
            if (isSuitableAdjacentWall) {
                return Optional.of(adjacentWall);
            }
        }

        return Optional.empty();
    }

    boolean isSuitableAdjacentWall(Point endOfWall) {
        return currentWall.isSuitableNextPoint(getPointInNewCoordinateAxis(endOfWall));
    }

    /**
     * Let's say we have a point(x, y) and we want to count its coordinates in new axes.
     * If we know a new X-axis angle and its coordinates (x0, y0),
     * then we could find new coordinates regarding the axis.
     *
     * @return a point in new axes of coordinates
     */
    private Point getPointInNewCoordinateAxis(Point pointToTransform) {
        Double k = currentWall.getLine().getK();
        double alpha;
        if (k == null) {
            // transformation from degrees to radians
            alpha = 90 * 2 * Math.PI / 360;
        } else {
            alpha = Math.atan(k);
        }

        double sinAlpha = Math.sin(alpha);
        double cosAlpha = Math.cos(alpha);

        double x0 = currentWall.getEnd().getX();
        double y0 = currentWall.getEnd().getY();

        double x = pointToTransform.getX();
        double y = pointToTransform.getY();

        double newX = (x - x0) * cosAlpha + (y - y0) * sinAlpha;
        double newY = -(x - x0) * sinAlpha + (y - y0) * cosAlpha;

        return new Point(newX, newY);
    }
}
