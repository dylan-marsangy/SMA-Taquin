package puzzle.agent.state;

import mailbox.Postman;
import mailbox.message.Message;
import mailbox.message.body.PushRequest;
import puzzle.Direction;
import puzzle.PuzzleLocation;
import puzzle.agent.AgentPiece;
import puzzle.agent.state.name.AutomateState;
import puzzle.agent.state.name.StateName;

import java.util.*;
import java.util.stream.Collectors;

@AutomateState(name = StateName.READING)
public class ReadingState extends State {

    private static final double PERCENTAGE_IGNORE_MESSAGE = 0.3;
    private boolean alreadyRead = false;

    public ReadingState(AgentPiece automate) {
        super(automate);
    }

    @Override
    public void execute() {
        if (!alreadyRead) {
            Message msg = Postman.getLastMessage(automate);
            if (!msg.isOutdated()) {
                if (new Random().nextDouble() > PERCENTAGE_IGNORE_MESSAGE) {
                    System.out.println(String.format("%s<>%s", automate, msg));

                    AgentPiece sender = (AgentPiece) msg.getSender();
                    PuzzleLocation exclusion = sender.getCurrentLocation();
                    PuzzleLocation goalRecipient = ((PushRequest) msg.getBody()).getDestinationLocation();
                    Direction direction = pickDirection(exclusion);

                    PuzzleLocation oldLocation = automate.getCurrentLocation();
                    boolean moved = automate.move(direction);
                    if (moved) {
                        if (automate.getCurrentLocation().equals(goalRecipient)) {
                            exclusion = oldLocation;
                            direction = pickDirection(exclusion);
                            moved = automate.move(direction);
                            if (moved) {
                                automate.setState(new WaitingState(automate, sender));
                            } else {
                                Message newMsg = sendMessage(direction);
                                automate.setState(new WaitingState(automate, (AgentPiece) newMsg.getRecipient()));
                                ((AgentPiece) newMsg.getRecipient()).execute();
                            }
                        } else {
                            automate.setState(new WaitingState(automate, sender));
                        }
                    } else {
                        // Si la pièce n'a pas pu bouger à la pièce qui bloque le passage dans la direction.
                        Message newMsg = sendMessage(direction);
                        automate.setState(new WaitingState(automate, (AgentPiece) newMsg.getRecipient()));
                        ((AgentPiece) newMsg.getRecipient()).execute();
                    }
                } else {
                    System.out.println(String.format("%s!<>!%s", automate, msg));
                    automate.setState(new MovingState(automate));
                }
            } else {
                automate.setState(new MovingState(automate));
            }

            alreadyRead = true;
        } else {
            automate.setState(new MovingState(automate));
        }
    }

    private Direction pickDirection(PuzzleLocation exclusion) {
        Set<Direction> freeNeighbors = getBestFreeNeighbors(exclusion);
        return freeNeighbors.stream()
                .skip(new Random().nextInt(freeNeighbors.size()))
                .findFirst()
                .get();
    }

    private Set<Direction> getBestFreeNeighbors(PuzzleLocation exclusion) {
        Set<Direction> freeNeighbors = automate.getPuzzle().getFreeNeighbors(automate.getCurrentLocation());
        if (freeNeighbors.isEmpty()) {
            return automate.getPossibleMoves().stream()
                    .filter(dir -> !automate.simulateMove(dir).equals(exclusion))
                    .collect(Collectors.toSet());
        }

        Map<Direction, Integer> mapDistances = new HashMap<>();
        freeNeighbors.forEach(direction -> {
            PuzzleLocation location = automate.simulateMove(direction);
            if (!location.equals(exclusion)) {
                Integer distance = automate.getPuzzle().getDistance(location, automate.getFinalLocation());
                mapDistances.put(direction, distance);
            }
        });

        int min = Collections.min(mapDistances.values());
        freeNeighbors = mapDistances.entrySet().stream()
                .filter(entry -> entry.getValue() == min)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return freeNeighbors;

    }

    private Message sendMessage(Direction direction) {
        PuzzleLocation nextMove = automate.getPuzzle().getNeighbor(automate.getCurrentLocation(), direction);
        PushRequest request = new PushRequest(
                automate,
                automate.getCurrentLocation(),
                automate.getFinalLocation());
        Message newMsg = new Message(
                automate,
                (AgentPiece) automate.getPuzzle().getPieceInLocation(nextMove),
                request);
        automate.sendMessage(newMsg);
        return newMsg;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
