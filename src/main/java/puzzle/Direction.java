package puzzle;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public enum Direction {

    UP(-1, 0),
//    UP_RIGHT(-1, 1),
    RIGHT(0, 1),
//    DOWN_RIGHT(1, 1),
    DOWN(1, 0),
//    DOWN_LEFT(1, -1),
    LEFT(0, -1),
//    UP_LEFT(-1, -1)
    ;

    private final int x;
    private final int y;

    private static Map<Pair<Integer, Integer>, Direction> mapValToDirection = new HashMap<>();

    static {
        for (Direction direction : Direction.values()) {
            mapValToDirection.put(new Pair<>(direction.x, direction.y), direction);
        }
    }

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction getInstance(int x, int y) throws IllegalArgumentException {
        Direction result = mapValToDirection.get(new Pair<>(x, y));
        if (result == null) throw new IllegalArgumentException(
                String.format("La direction (%s, %s) n'existe pas", x, y));

        return result;
    }

    /**
     * Renvoie la direction pour aller de la case de départ à l'arrivée.
     *
     * @param start Case de départ.
     * @param goal  Case d'arrivée.
     * @return (1) Either the direction to go from the start location to goal location
     * or (2) null if the locations are not adjacent.
     */
    public static Direction getDirection(PuzzleLocation start, PuzzleLocation goal) {
        try {
            return getInstance(goal.getX() - start.getX(), goal.getY() - start.getY());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
