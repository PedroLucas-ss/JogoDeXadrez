package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Colors;

import java.util.Scanner;

public class Rook extends ChessPiece {
    public Rook(Board board, Colors color) {
        super(board, color);
    }
    @Override
    public String toString(){
        return "R";
    }

    @Override
    public boolean[][] possivelMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return mat;
    }
}
