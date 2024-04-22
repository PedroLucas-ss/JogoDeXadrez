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

    @Override
    public boolean[][] possivelMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return mat;
    }
}
