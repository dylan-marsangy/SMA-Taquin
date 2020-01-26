package puzzle.pathfinding;

import puzzle.Direction;
import puzzle.Puzzle;
import puzzle.PuzzleLocation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaivePathFinder implements PathFinder {

    private Puzzle maze;

    public NaivePathFinder(Puzzle maze) {
        this.maze = maze;
    }

    @Override
    public Set<Direction> findNextDirTo(PuzzleLocation start, PuzzleLocation goal) {
        int xDistance = goal.getX() - start.getX();
        int yDistance = goal.getY() - start.getY();
        Direction horizontalDirection =
                (yDistance == 0) ? null :
                        (yDistance > 0) ? Direction.RIGHT : Direction.LEFT;
        Direction verticalDirection =
                (xDistance == 0) ? null :
                        (xDistance < 0) ? Direction.UP : Direction.DOWN;

        return Stream.of(horizontalDirection, verticalDirection)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public LinkedList<PuzzleLocation> findPathTo(PuzzleLocation start, PuzzleLocation goal) {
        LinkedList<PuzzleLocation> path = new LinkedList<>();
        path.add(start);

        int xDistance = goal.getX() - start.getX();
        int yDistance = goal.getY() - start.getY();
        Direction horizontalDirection = (yDistance > 0) ? Direction.RIGHT : Direction.LEFT;
        Direction verticalDirection = (xDistance < 0) ? Direction.UP : Direction.DOWN;

        PuzzleLocation current = start;
        for (int i = 1; i <= Math.abs(yDistance); i++) {
            current = maze.getNeighbor(current, horizontalDirection);
            path.add(current);
        }

        for (int i = 1; i <= Math.abs(xDistance); i++) {
            current = maze.getNeighbor(current, verticalDirection);
            path.add(current);
        }

        return path;
    }
}
