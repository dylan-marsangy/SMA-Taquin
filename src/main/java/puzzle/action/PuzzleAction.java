package puzzle.action;

import puzzle.Direction;
import puzzle.Piece;
import puzzle.Puzzle;
import puzzle.PuzzleLocation;

public class PuzzleAction {

    private Piece piece;
    private PuzzleLocation startLocation;
    private Direction direction;

    public PuzzleAction(Piece piece, PuzzleLocation startLocation, Direction direction) {
        this.piece = piece;
        this.startLocation = startLocation;
        this.direction = direction;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public PuzzleLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(PuzzleLocation startLocation) {
        this.startLocation = startLocation;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
