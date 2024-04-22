package boardgame;

import java.util.Scanner;

public abstract class Piece {

    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;

    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possivelMoves();

    public boolean possivelMove(Position position){

        return possivelMoves()[position.getRow()][position.getColumn()];
    }
    public boolean isThereAnyPossibleMoves(){

        boolean[][] mat = possivelMoves();

        for(int i = 0 ; i < mat.length; i++){
            for (int j = 0 ; j < mat.length; j++){
                if (mat[i][j]){
                    return true;
                }
            }
        }
        return false;
    }

}
