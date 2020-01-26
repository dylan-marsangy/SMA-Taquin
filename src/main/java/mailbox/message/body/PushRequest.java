package mailbox.message.body;

import mailbox.message.MessageBody;
import mailbox.message.type.MessageType;
import mailbox.message.type.RequestType;
import puzzle.Piece;
import puzzle.PuzzleLocation;

@MessageType(type = RequestType.PUSH)
public class PushRequest extends MessageBody {

    private Piece piece;
    private PuzzleLocation startLocation;
    private PuzzleLocation destinationLocation;

    public PushRequest(Piece piece, PuzzleLocation startLocation, PuzzleLocation destinationLocation) {
        this.piece = piece;
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
    }

    @Override
    protected boolean isOutdated() {
        return !this.piece.getCurrentLocation().equals(startLocation);
    }

    @Override
    public PushRequest copy() {
        return new PushRequest(this.piece, this.startLocation, this.destinationLocation);
    }

    @Override
    public String toString() {
        return String.format("%s@%s->%s", piece, startLocation, destinationLocation);
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

    public PuzzleLocation getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(PuzzleLocation destinationLocation) {
        this.destinationLocation = destinationLocation;
    }
}
