package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

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

    protected boolean isThereOpponentPiece(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}
