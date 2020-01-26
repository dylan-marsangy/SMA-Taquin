package puzzle.specials;

import puzzle.Puzzle;
import puzzle.agent.AgentPiece;
import view.View;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PuzzlePushRequest extends Puzzle {

    private static final int DIMENSION = 3;
    private static final int TOTAL_PIECES = 2;

    public PuzzlePushRequest(View view) {
        super(view, DIMENSION, TOTAL_PIECES);
    }

    @Override
    protected Set<AgentPiece> generatePieces(int totalPieces) throws IllegalArgumentException {
        return Stream.of(
                new AgentPiece(view, this, getLocation(1, 1), getLocation(1, 3)),
                new AgentPiece(view, this, getLocation(1, 2), getLocation(1, 2))
        ).collect(Collectors.toSet());
    }
}
