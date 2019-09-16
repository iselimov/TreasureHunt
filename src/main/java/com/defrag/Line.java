package com.defrag;

import java.util.*;

/**
 * Represents a math line functionality.
 * Equation has view as y = kx + b.
 *
 * 2 partial cases:
 * If we have a line which is parallel to X-axis, then we have k = 0
 * if the line is parallel to Y-axis, then both k and b are null whereas x is not null .
 */
class Line {

    private final Double k;
    private final Double b;

    private final Double x;

    private final List<Point> points;

    private Line(Builder builder) {
        k = builder.k;
        b = builder.b;
        x = builder.x;

        points = new ArrayList<>(builder.points);
    }

    List<Wall> buildWalls() {
        List<Wall> result = new ArrayList<>();

        for (int i = 0, j = i + 1; j < points.size(); i++, j++) {
            result.add(new Wall(this, points.get(i), points.get(j)));
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Line)) {
            return false;
        }

        Line other = (Line) obj;
        return Objects.equals(k, other.k)
                && Objects.equals(b, other.b)
                && Objects.equals(x, other.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, b, x);
    }

    boolean isBottomBorder() {
        return k != null && k == 0 &&
                b != null && b == InputDataParser.BOTTOM_LEFT_POINT.getY();
    }

    boolean isTopBorder() {
        return k != null && k == 0 &&
                b != null && b == InputDataParser.TOP_RIGHT_POINT.getY();
    }

    boolean isLeftBorder() {
        return x != null && x == InputDataParser.BOTTOM_LEFT_POINT.getX();
    }

    boolean isRightBorder() {
        return x != null && x == InputDataParser.TOP_RIGHT_POINT.getX();
    }

    Double getK() {
        return k;
    }

    static class Builder {

        private final Double k;
        private final Double b;

        private final Double x;

        /**
         * We fill up this set in the particular order,
         * it will help us to build an adjacent list for graph presentation in the future.
         */
        private Set<Point> points = new TreeSet<>(
                Comparator.comparingDouble(Point::getX).thenComparingDouble(Point::getY));

        Builder(Point start, Point end) {
            if (start.equals(end)) {
                throw new IllegalArgumentException("Points must be different!");
            }

            if (start.getX() == end.getX()) {
                x = start.getX();
                k = null;
                b = null;
            } else {
                x = null;
                k = (end.getY() - start.getY()) / (end.getX() - start.getX());
                b = start.getY() - start.getX() * (end.getY() - start.getY()) / (end.getX() - start.getX());
            }
        }

        Optional<Point> getIntersectedPoint(Builder other) {
            if (Objects.equals(k, other.k)) {
                return Optional.empty();
            }

            double resultX;
            double resultY;
            if (k == null) {
                resultX = x;
                resultY = other.k * resultX + other.b;
            } else if (other.k == null) {
                resultX = other.x;
                resultY = k * resultX + b;
            } else {
                resultX = (other.b - b) / (k - other.k);
                resultY = k * resultX + b;
            }

            return Optional.of(new Point(resultX, resultY))
                    // we don't need intersected points which locate out of the considered area
                    .filter(curr -> curr.isHigherThan(InputDataParser.BOTTOM_LEFT_POINT))
                    .filter(curr -> curr.isLowerThan(InputDataParser.TOP_RIGHT_POINT));
        }

        Builder addBelongingPoint(Point point) {
            points.add(point);
            return this;
        }

        Line build() {
            return new Line(this);
        }
    }
}
