package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

import java.awt.*;
import java.util.Scanner;

public class ChessMatch {


    private Board board;
    public ChessMatch(){

        board = new Board(8,8);
        inicialSetup();
    }
    public ChessPiece[][] getpieces(){

        ChessPiece[][] mat =  new ChessPiece[board.getRows()][board.getColumns()];
        for(int i = 0 ; i< board.getRows(); i++){
            for (int j=0 ; j< board.getColumns(); j++){
                mat[i][j] =(ChessPiece) board.piece(i,j);
            }
        }
        return mat;
    }
    public ChessPiece performeChessMove(ChessPosition sourcePosition,ChessPosition targetPosition){

        Position source = sourcePosition.toPositon();
        Position taget = targetPosition.toPositon();
        validateSourcePosition(source);
        Piece capturedPiece = makeMove(source,taget);
        return (ChessPiece) capturedPiece;

    }

    private Piece makeMove(Position source, Position target){
        Piece p =board.removePiece(source);
        Piece capturePiece = board.removePiece(target);
        board.placePiece(p,target);
        return capturePiece;
    }
    private void validateSourcePosition(Position position){
        if(!board.thereIsApiece(position)) {
        throw new ChessException("Nao existe peca na posicao de origem");

        }

    }
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPositon());

    }
    private void inicialSetup(){
        placeNewPiece('c', 1, new Rook(board, Colors.White));
        placeNewPiece('c', 2, new Rook(board, Colors.White));
        placeNewPiece('d', 2, new Rook(board, Colors.White));
        placeNewPiece('e', 2, new Rook(board, Colors.White));
        placeNewPiece('e', 1, new Rook(board, Colors.White));
        placeNewPiece('d', 1, new King(board, Colors.White));

        placeNewPiece('c', 7, new Rook(board, Colors.Black));
        placeNewPiece('c', 8, new Rook(board, Colors.Black));
        placeNewPiece('d', 7, new Rook(board, Colors.Black));
        placeNewPiece('e', 7, new Rook(board, Colors.Black));
        placeNewPiece('e', 8, new Rook(board, Colors.Black));
        placeNewPiece('d', 8, new King(board, Colors.Black));


    }

}
