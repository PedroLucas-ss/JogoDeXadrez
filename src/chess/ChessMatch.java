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

    public boolean[][] possivelMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPositon();
        validateSourcePosition(position);
        return board.piece(position).possivelMoves();

    }
    public ChessPiece performeChessMove(ChessPosition sourcePosition,ChessPosition targetPosition){

        Position source = sourcePosition.toPositon();
        Position target = targetPosition.toPositon();
        validateSourcePosition(source);
        ValidateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source,target);
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
        if(!board.piece(position).isThereAnyPossibleMoves()){
            throw new ChessException("Nao existe movimentos possiveis para essa peca, escolha outro");

        }

    }
    private void ValidateTargetPosition(Position source, Position target){
        if(!board.piece(source).possivelMove(target)){
            throw new ChessException("A peca escolhida nao pode se mover para posicao de destino");
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
