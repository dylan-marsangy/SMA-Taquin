package puzzle.agent.state;

import mailbox.message.Message;
import mailbox.message.body.PushRequest;
import puzzle.Direction;
import puzzle.PuzzleLocation;
import puzzle.agent.AgentPiece;
import puzzle.agent.state.name.AutomateState;
import puzzle.agent.state.name.StateName;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@AutomateState(name = StateName.MOVING)
public class MovingState extends State {

    protected int counter = 0;

    private static final double PERCENTAGE_RANDOM_MOVE = 0.1;

    public MovingState(AgentPiece automate) {
        super(automate);
    }

    @Override
    public void execute() {
        if (!automate.isAtFinalLocation()) {
            counter++;

            boolean moved = false;
            Direction firstPossibleMove;

            // Faire un move aléatoire avec un certain pourcentage de chance.
            if (new Random().nextDouble() < PERCENTAGE_RANDOM_MOVE) {
                firstPossibleMove = getRandomMove();
                moved = automate.move(firstPossibleMove);
            } else {
                // Sinon, meilleur déplacement possible.
                Set<Direction> possibleMoves = automate.findNextDirTo(automate.getFinalLocation());
                firstPossibleMove = possibleMoves.stream()
                        .skip(new Random().nextInt(possibleMoves.size()))
                        .findFirst()
                        .get();

                while (!possibleMoves.isEmpty() || moved) {
                    Direction nextMove = possibleMoves.stream()
                            .skip(new Random().nextInt(possibleMoves.size()))
                            .findFirst()
                            .get();
                    possibleMoves.remove(nextMove);

                    moved = automate.move(nextMove);
                }
            }

            // Si la pièce n'a pas pu bouger.
            if (moved) {
               if (automate.isAtFinalLocation()) {
                   automate.setState(new SleepingState(automate));
               }
            } else {
                sendMessage(firstPossibleMove);
            }
        }
    }

    private Direction getRandomMove() {
        Direction[] allDirections = Direction.values();
        return allDirections[new Random().nextInt(allDirections.length)];

    }

    protected Message sendMessage(Direction direction) {
        PuzzleLocation nextMove = automate.getPuzzle().getNeighbor(automate.getCurrentLocation(), direction);
        AgentPiece obstacle = (AgentPiece) automate.getPuzzle().getPieceInLocation(nextMove);
        PushRequest request = new PushRequest(
                automate,
                automate.getCurrentLocation(),
                automate.getFinalLocation());
        Message msg = new Message(
                automate,
                obstacle,
                request);
        if (!(obstacle.getState() instanceof HoldingState))
            automate.sendMessage(msg);
        return msg;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
