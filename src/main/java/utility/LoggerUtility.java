package utility;

import puzzle.Piece;
import puzzle.PuzzleLocation;

public class LoggerUtility {

    public static void log(Piece piece) {
        System.out.println(String.format("%s in %s for %s", piece, piece.getCurrentLocation(), piece.getFinalLocation()));
    }

    public static void log(Piece piece, PuzzleLocation oldLocation, PuzzleLocation newLocation) {
        System.out.println(String.format("%s moved from %s to %s", piece, oldLocation, newLocation));
    }

}
