
import mailbox.message.Message;
import mailbox.Postman;
import mailbox.message.body.PushRequest;
import puzzle.Puzzle;
import puzzle.agent.AgentPiece;
import view.View;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final int PUZZLE_SIZE = 5;
    private static final int NUMBER_PIECES = 4;

    public static void main(String[] args) {
        System.out.println("INITIALISATION DU PUZZLE");
        Puzzle maze = new Puzzle(new View(), PUZZLE_SIZE, NUMBER_PIECES);
        Set<AgentPiece> pieces = maze.getPieces();
        System.out.println(maze);

//        System.out.println("Configuration des pièces :");
//        pieces.forEach(LoggerUtility::log);
//        System.out.println();

        System.out.println("Test de mailing :");
        Iterator<AgentPiece> iter = pieces.iterator();
        AgentPiece sender = iter.next();
        AgentPiece receiver = iter.next();
        Set<Message> messages = Stream.of(
                new Message(sender, receiver, new PushRequest(sender, sender.getCurrentLocation(), sender.getFinalLocation())),
                new Message(sender, receiver, new PushRequest(sender, sender.getCurrentLocation(), sender.getFinalLocation())),
                new Message(sender, receiver, new PushRequest(sender, sender.getCurrentLocation(), sender.getFinalLocation())),
                new Message(sender, receiver, new PushRequest(sender, sender.getCurrentLocation(), sender.getFinalLocation())),
                new Message(sender, receiver, new PushRequest(sender, sender.getCurrentLocation(), sender.getFinalLocation()))
        ).collect(Collectors.toSet());
        messages.forEach(message -> message.getSender().sendMessage(message));
        Set<Message> mailBox = Postman.getMessages(receiver);
        mailBox.forEach(System.out::println);

        System.out.println();

//        System.out.print("Test de parcours : ");
//        try {
//            Piece pieceToMove = pieces.iterator().next();
//            System.out.println(String.format("%s -> %s", pieceToMove.getCurrentLocation(), pieceToMove.getFinalLocation()));
//            LinkedList<PuzzleLocation> result = pieceToMove.findPathTo(pieceToMove.getFinalLocation());
//            String s = result.stream().map(PuzzleLocation::toString).collect(Collectors.joining(", "));
//            System.out.println(String.format("[%s]", s));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
