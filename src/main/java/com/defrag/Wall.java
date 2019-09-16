package com.defrag;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents wall within the pyramid.
 */
class Wall {

    private final Line line;

    private final Point start;
    private final Point end;

    /**
     * Here we store information about an existing link with external world,
     * which is used in seeking an exit.
     */
    private final boolean isExterior;

    /**
     * This parameter helps us to find the next suitable point while graph traversing.
     *
     * @see DirectionType
     */
    private final DirectionType directionType;

    Wall(Line line, Point start, Point end) {
        this.line = line;
        this.start = start;
        this.end = end;

        isExterior = line.isBottomBorder()
                || line.isRightBorder()
                || line.isTopBorder()
                || line.isLeftBorder();

        directionType = defineDirectionType(line, start, end);
    }

    Wall createReverse() {
        return new Wall(line, end, start);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Wall)) {
            return false;
        }

        Wall other = (Wall) obj;
        return start.equals(other.start) && end.equals(other.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    boolean isExterior() {
        return isExterior;
    }

    Line getLine() {
        return line;
    }

    Point getStart() {
        return start;
    }

    Point getEnd() {
        return end;
    }

    boolean hasOppositeDirectionTo(Wall other) {
        return start.equals(other.end) && other.start.equals(end);
    }

    boolean isNotCoDirected() {
        return directionType == DirectionType.NOT_CO_DIRECTED;
    }

    boolean isSuitableNextPoint(Point newPoint) {
        return directionType.filter(newPoint);
    }

    private DirectionType defineDirectionType(Line line, Point start, Point end) {
        if (line.getK() == null) {
            return start.getY() < end.getY()
                    ? DirectionType.CO_DIRECTED
                    : DirectionType.NOT_CO_DIRECTED;
        }

        if (line.getK() == 0) {
            return start.getX() < end.getX()
                    ? DirectionType.CO_DIRECTED
                    : DirectionType.NOT_CO_DIRECTED;
        }

        double kMultipliedWithAxisYShift = line.getK() * (end.getY() - start.getY());
        return Math.signum(kMultipliedWithAxisYShift) > 0
                ? DirectionType.CO_DIRECTED
                : DirectionType.NOT_CO_DIRECTED;
    }

    /**
     * If a line is an increasing function and subtraction of end and start point is positive,
     * then direction is CO_DIRECTED. The same is for decreasing function and negative subtraction.
     * Otherwise direction type is NOT_CO_DIRECTED.
     * <p>
     * If the line is CO_DIRECTED then the next point will be located over it, and vice versa
     * the point will be located under it for NOT_CO_DIRECTED one.
     */
    private enum DirectionType {
        CO_DIRECTED(p -> p.getY() > 0),
        NOT_CO_DIRECTED(p -> p.getY() < 0);

        private final Predicate<Point> filterPredicate;

        DirectionType(Predicate<Point> filterPredicate) {
            this.filterPredicate = filterPredicate;
        }

        boolean filter(Point newPoint) {
            return filterPredicate.test(newPoint);
        }
    }
}
