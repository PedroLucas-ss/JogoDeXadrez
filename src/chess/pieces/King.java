package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Colors;

import java.util.Scanner;

public class King extends ChessPiece {


    public King(Board board, Colors color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return "K";
    }
}
