package puzzle;

import javafx.scene.paint.Color;
import puzzle.action.ActionObservable;
import puzzle.action.ActionObserver;
import puzzle.action.IllegalActionException;
import puzzle.action.PuzzleAction;
import view.ColorGenerator;
import view.View;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class Piece implements ActionObservable {

    private static Long numberInstances = 0L;

    private Long id;
    private PuzzleLocation currentLocation;
    private final PuzzleLocation finalLocation;

    private Puzzle puzzle;

    // View
    private View view;
    private Color color;

    public Piece(View view, Puzzle puzzle, PuzzleLocation startLocation, PuzzleLocation finalLocation) {
        numberInstances++;
        this.id = numberInstances;

        this.puzzle = puzzle;

        this.currentLocation = startLocation;
        this.finalLocation = finalLocation;

        this.view = view;
        this.color = ColorGenerator.getRandomColor();
    }

    public Set<Direction> findNextDirTo(PuzzleLocation goal) {
        return this.getPuzzle().getPathFinder().findNextDirTo(currentLocation, goal);
    }

    public Set<Direction> getPossibleMoves() {
        return Arrays.stream(Direction.values())
                .filter(dir -> puzzle.isInsideBoard(simulateMove(dir)))
                .collect(Collectors.toSet());
    }

    public PuzzleLocation simulateMove(Direction direction) {
        return puzzle.calculateNewLocation(new PuzzleAction(this, this.currentLocation, direction));
    }

    public synchronized boolean move(Direction direction) {
        PuzzleAction action = new PuzzleAction(this, this.getCurrentLocation(), direction);

        try {
            PuzzleLocation goalLocation = puzzle.getActionResult(action);
            setCurrentLocation(goalLocation);
            System.out.println(String.format("%s@%s->(%s)%s", this, action.getStartLocation(), direction, currentLocation));

            notifyActionObserver(puzzle.getView());
            puzzle.getPieces().forEach(p -> notifyActionObserver(p));
        } catch (IllegalActionException e) {
            System.out.println(String.format("%s@(%s)%s", this, direction, currentLocation));
            return false;
        }

        return true;
    }

    public boolean isAtFinalLocation() {
        return currentLocation.equals(finalLocation);
    }

    @Override
    public void notifyActionObserver(ActionObserver... observers) {
        Arrays.stream(observers)
                .forEach(actionObserver -> actionObserver.updateActionObserver(this));
    }

    @Override
    public String toString() {
        return String.format("P%d", id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        return (id.equals(piece.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Long getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public PuzzleLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(PuzzleLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public PuzzleLocation getFinalLocation() {
        return finalLocation;
    }

}