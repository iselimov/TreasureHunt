package com.defrag;

import java.util.Objects;

/**
 * Represents a point within the coordinate axes.
 */
class Point {

    private final double x;
    private final double y;

    Point(double x, double y) {
        // it prevents the problem with hashCode calculation
        // 0.0.hashCode != -0.0.hashCode()
        this.x = x == -0 ? 0 : x;
        this.y = y == -0 ? 0 : y;
    }

    boolean isHigherThan(Point other) {
        return x >= other.getX() && y >= other.getY();
    }

    boolean isLowerThan(Point other) {
        return x <= other.getX() && y <= other.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }
}
