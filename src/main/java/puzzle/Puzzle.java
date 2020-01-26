package puzzle;

import puzzle.action.IllegalActionException;
import puzzle.action.PuzzleAction;
import puzzle.agent.AgentPiece;
import puzzle.pathfinding.NaivePathFinder;
import puzzle.pathfinding.PathFinder;
import view.View;

import java.util.*;
import java.util.stream.Collectors;

public class Puzzle {

    protected View view;
    protected int dimension;
    protected PuzzleLocation[][] grid;
    protected Set<AgentPiece> pieces;
    private PathFinder pathFinder;

    public Puzzle(View view, int dimension, int totalPieces) {
        this.view = view;

        // Génération de la grille.
        this.dimension = dimension;
        this.grid = generateGrid(dimension);

        // Génération des pièces.
        this.pieces = generatePieces(totalPieces);

        this.pathFinder = new NaivePathFinder(this);

        System.out.println(String.format("Plateau rempli à %d%%",
                (totalPieces * 100) / (dimension * dimension)));
    }

    private PuzzleLocation[][] generateGrid(int dimension) {
        PuzzleLocation[][] grid = new PuzzleLocation[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j] = new PuzzleLocation(i + 1, j + 1);
            }
        }

        return grid;
    }

    protected Set<AgentPiece> generatePieces(int totalPieces) throws IllegalArgumentException {
        Set<AgentPiece> pieces = new HashSet<>();
        AgentPiece piece;

        if (totalPieces >= dimension * dimension)
            throw new IllegalArgumentException(String.format(
                    "Le plateau est de taille %dx%d (%d cases), vous ne pouvez donc placer qu'au maximum %d-1=%d pièces maximum.",
                    dimension, dimension, dimension * dimension, dimension * dimension, dimension * dimension - 1
            ));

        PuzzleLocation startLocation;
        PuzzleLocation finalLocation;
        List<PuzzleLocation> startLocations = Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        List<PuzzleLocation> finalLocations = Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        for (int i = 0; i < totalPieces; i++) {
            Collections.shuffle(startLocations);
            Collections.shuffle(finalLocations);

            startLocation = startLocations.remove(0);
            finalLocation = finalLocations.remove(0);

            piece = new AgentPiece(view, this, startLocation, finalLocation);
            pieces.add(piece);
        }

        return pieces;
    }

    public synchronized PuzzleLocation getActionResult(PuzzleAction action) throws IllegalActionException {
        PuzzleLocation goalLocation = calculateNewLocation(action);

        if (!isInsideBoard(action.getStartLocation()))
            throw new IllegalActionException(String.format("La case de départ %s n'existe pas.", action.getStartLocation()));
        if (!isInsideBoard(goalLocation))
            throw new IllegalActionException(String.format("La case d'arrivée %s n'existe pas.", goalLocation));
        if (isNotEmpty(goalLocation))
            throw new IllegalActionException(String.format("Collision en %s", goalLocation));

        return goalLocation;
    }

    public PuzzleLocation calculateNewLocation(PuzzleAction action) {
        return getLocation(
                action.getStartLocation().getX() + action.getDirection().getX(),
                action.getStartLocation().getY() + action.getDirection().getY());
    }

    public Set<Direction> getFreeNeighbors(PuzzleLocation start) {
        return Arrays.stream(Direction.values())
                .filter(direction -> {
                    PuzzleLocation location = getNeighbor(start, direction);
                    return location != null && getPieceInLocation(location) == null;
                })
                .collect(Collectors.toSet());
    }

    public PuzzleLocation getNeighbor(PuzzleLocation start, Direction direction) {
        int endX = start.getX() + direction.getX();
        int endY = start.getY() + direction.getY();

        if (!isInsideBoard(endX, endY)) return null;

        return getLocation(endX, endY);
    }

    public Piece getPieceInLocation(PuzzleLocation location) {
        return pieces.stream()
                .filter(p -> p.getCurrentLocation().equals(location))
                .findFirst()
                .orElse(null);
    }

    public int getDistance(PuzzleLocation start, PuzzleLocation goal) {
        return (Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY()));
    }

    // Indique si la PuzzleLocation existe sur le plateau ou non.
    public boolean isInsideBoard(PuzzleLocation location) {
        return isInsideBoard(location.getX(), location.getY());
    }

    // Coordonnées d'une PuzzleLocation.
    private boolean isInsideBoard(int x, int y) {
        return x >= 1 && x <= dimension && y >= 1 && y <= dimension;
    }

    // Coordonnées d'une PuzzleLocation.
    public PuzzleLocation getLocation(int x, int y) {
        return this.grid[x - 1][y - 1];
    }

    /**
     * Indique si le puzzle est fini ou non.
     * @return True si toutes les pièces sont à leur destination finale, false sinon.pus
     */
    public boolean isResolved() {
        return pieces.stream()
                .allMatch(Piece::isAtFinalLocation);
    }

    public boolean isNotEmpty(PuzzleLocation location) {
        return pieces.stream()
                .anyMatch(piece -> piece.getCurrentLocation().equals(location));
    }

    @Override
    public String toString() {
        Map<PuzzleLocation, Long> piecesLocations = pieces.stream()
                .collect(Collectors.toMap(Piece::getCurrentLocation, Piece::getId));

       StringBuilder sb = new StringBuilder();
        for (PuzzleLocation[] rows : grid) {
            for (PuzzleLocation location : rows) {
                if (piecesLocations.containsKey(location)) {
                    sb.append(String.format(" %d ", piecesLocations.get(location)));
                } else {
                    sb.append("   ");
                }

                sb.append("|");

                if (location.getY() == dimension) {
                    sb.append("\n");
                }
            }
        }

        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public PuzzleLocation[][] getGrid() {
        return grid;
    }

    public void setGrid(PuzzleLocation[][] grid) {
        this.grid = grid;
    }

    public Set<AgentPiece> getPieces() {
        return pieces;
    }

    public void setPieces(Set<AgentPiece> pieces) {
        this.pieces = pieces;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }

    public void setPathFinder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

}
