package puzzle.pathfinding;

import puzzle.Direction;
import puzzle.PuzzleLocation;

import java.util.Set;

public interface PathFinder {

    Set<Direction> findNextDirTo(PuzzleLocation start, PuzzleLocation goal);

}
