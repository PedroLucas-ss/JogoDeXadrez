package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

import java.util.Scanner;

public  abstract class ChessPiece extends Piece {

    private Colors color;
    protected int moveCount;
    public ChessPiece(Board board, Colors color) {
        super(board);
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }
    public int getMoveCount(){
        return moveCount;
    }

    public void increaseMoveCount(){
        moveCount++;
    }
    public void decreaseMoveCount(){
        moveCount--;
    }

    protected boolean isThereOpponentPiece(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
    public ChessPosition getChessPosition(){
        return ChessPosition.fromPosition(position);

    }
}
