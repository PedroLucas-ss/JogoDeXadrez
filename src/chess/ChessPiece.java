package chess;

import boardgame.Board;
import boardgame.Piece;

import java.util.Scanner;

public  abstract class ChessPiece extends Piece {

    private Colors color;
    public ChessPiece(Board board, Colors color) {
        super(board);
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }


}
