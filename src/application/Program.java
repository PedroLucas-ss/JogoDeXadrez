package application;

import boardgame.Board;
import boardgame.BoardException;
import boardgame.Position;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
public class Program {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> capturadas = new ArrayList<>();

        while (!chessMatch.getCheckMate()) {
            try {

                UI.clearScreen();
                UI.printMatch(chessMatch, capturadas);
                System.out.println();
                System.out.println("Sorce: ");
                ChessPosition source = UI.readChessPosition(scan);
                boolean[][] possivelMoves = chessMatch.possivelMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getpieces(),possivelMoves);
                System.out.println();
                System.out.println("Target: ");
                ChessPosition target = UI.readChessPosition(scan);

                ChessPiece capturePiece = chessMatch.performeChessMove(source, target);
                if(capturePiece != null){
                    capturadas.add(capturePiece);
                }
                if (chessMatch.getPromoted() != null) {
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    String type = sc.nextLine();
                    chessMatch.replacePromotedPiece(type);
                }
            }
            catch (ChessException e){
                System.out.println(e.getMessage());
                scan.nextLine();
            }
            catch (InputMismatchException e){
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch, capturadas);
    }

}
