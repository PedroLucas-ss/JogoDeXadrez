package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Colors;

import java.awt.*;
import java.util.Scanner;

public class King extends ChessPiece {

    private ChessMatch chessMatch;
    public King(Board board, Colors color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString(){
        return "K";
    }
    private boolean canMove(Position position){

        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }
    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

    @Override
    public boolean[][] possivelMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];


        Position p = new Position(0,0);
        //cima
        p.setValues(position.getRow()-1, position.getColumn());
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        //baixo
        p.setValues(position.getRow()+1, position.getColumn());
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        //esq
        p.setValues(position.getRow(), position.getColumn()-1);
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        //dir
        p.setValues(position.getRow(), position.getColumn()+1);
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        //esq-cima
        p.setValues(position.getRow()-1, position.getColumn()-1);
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        //dir-cima
        p.setValues(position.getRow()-1, position.getColumn()+1);
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        //esq-baixo
        p.setValues(position.getRow()+1, position.getColumn()-1);
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        //esq-baixo
        p.setValues(position.getRow()+1, position.getColumn()+1);
        if(getBoard().positionExists(p)&& canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            // #specialmove castling kingside rook
            Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(posT1)) {
                Position p1 = new Position(position.getRow(), position.getColumn() + 1);
                Position p2 = new Position(position.getRow(), position.getColumn() + 2);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    mat[position.getRow()][position.getColumn() + 2] = true;
                }
            }
            // #specialmove castling queenside rook
            Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(posT2)) {
                Position p1 = new Position(position.getRow(), position.getColumn() - 1);
                Position p2 = new Position(position.getRow(), position.getColumn() - 2);
                Position p3 = new Position(position.getRow(), position.getColumn() - 3);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
                    mat[position.getRow()][position.getColumn() - 2] = true;
                }
            }
        }




        return mat;


    }
}
