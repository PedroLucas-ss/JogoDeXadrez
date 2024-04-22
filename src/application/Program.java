package application;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Scanner;
public class Program {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while (true) {

            UI.printBoard(chessMatch.getpieces());
            System.out.println();
            System.out.println("Sorce: ");
            ChessPosition source = UI.readChessPosition(scan);
            System.out.println();
            System.out.println("Target: ");
            ChessPosition target = UI.readChessPosition(scan);

            ChessPiece capturePiece = chessMatch.performeChessMove(source,target);

        }
        }

}
