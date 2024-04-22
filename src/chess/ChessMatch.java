package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ChessMatch {

    private int turn;
    private Colors currentPlayer;
    private Board board;
    private List<Piece> piecesOnTheBord = new ArrayList<>();
    private List<Piece> capturadPieces = new ArrayList<>();
    private boolean check;
    private boolean checkMate;
    public ChessMatch(){

        board = new Board(8,8);

        turn =1;
        currentPlayer =Colors.White;

        inicialSetup();
    }
    public int getTurn(){
        return turn;
    }
    public Colors getCurrentPlayer(){
        return currentPlayer;
    }
    public boolean getCheck(){
        return check;
    }
    public boolean getCheckMate(){
        return checkMate;
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

        if(testCheck(currentPlayer)){
            undoMove(source,target,capturedPiece);
            throw new ChessException("Voce nao pode se colocar em Check");
        }
        check =(testCheck(opponent(currentPlayer)))? true : false;
        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;

    }

    private Piece makeMove(Position source, Position target){
        ChessPiece p =(ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturePieces = board.removePiece(target);
        board.placePiece(p,target);

        if (capturePieces != null){
            piecesOnTheBord.remove(capturePieces);
            capturadPieces.add(capturePieces);
        }
        return capturePieces;
    }
    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p =(ChessPiece) board.removePiece(source);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if(capturedPiece != null){
            board.placePiece(capturedPiece, target);
            capturadPieces.remove(capturedPiece);
            piecesOnTheBord.add(capturedPiece);
        }
    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsApiece(position)) {
        throw new ChessException("Nao existe peca na posicao de origem");

        }
        if(currentPlayer != ((ChessPiece) board.piece(position)).getColor()){
            throw new ChessException("a peca escolhida nao e sua");
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
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Colors.White)? Colors.Black : Colors.White;
    }

    private Colors opponent(Colors colors){
        return (colors == Colors.White) ? Colors.Black : Colors.White;
    }
    private ChessPiece king(Colors colors){
        List<Piece> list = piecesOnTheBord.stream().filter(x->((ChessPiece)x).getColor()==colors).collect(Collectors.toList());
        for(Piece p : list){
            if(p instanceof King){
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Nao existe o  rei " + colors + " no tabuleiro");
    }

    private boolean testCheck(Colors colors){
        Position kingPosition = king(colors).getChessPosition().toPositon();
        List<Piece> opponentPieces = piecesOnTheBord.stream().filter(x->((ChessPiece)x).getColor()==opponent(colors)).collect(Collectors.toList());
        for( Piece p : opponentPieces){
            boolean[][] mat= p.possivelMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Colors colors){
        if(!testCheck(colors)){
            return false;
        }
        List<Piece> list = piecesOnTheBord.stream().filter(x->((ChessPiece)x).getColor()==colors).collect(Collectors.toList());
        for (Piece p : list){
            boolean[][] mat = p.possivelMoves();
            for(int i=0; i < board.getRows(); i++){
                for(int j=0;j< board.getColumns(); j++){
                    if(mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPositon();
                        Position target = new Position(i,j);
                        Piece capturedPiece = makeMove(source,target);
                        boolean testCheck = testCheck(colors);
                        undoMove(source,target,capturedPiece);
                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPositon());
        piecesOnTheBord.add(piece);

    }
    private void inicialSetup(){
        placeNewPiece('h', 7, new Rook(board, Colors.White));
        //placeNewPiece('c', 2, new Rook(board, Colors.White));
        // placeNewPiece('d', 2, new Rook(board, Colors.White));
        // placeNewPiece('e', 2, new Rook(board, Colors.White));
        placeNewPiece('d', 1, new Rook(board, Colors.White));
        placeNewPiece('e', 1, new King(board, Colors.White));

        //placeNewPiece('c', 7, new Rook(board, Colors.Black));
        //placeNewPiece('c', 8, new Rook(board, Colors.Black));
        // placeNewPiece('d', 7, new Rook(board, Colors.Black));
        //placeNewPiece('e', 7, new Rook(board, Colors.Black));
        placeNewPiece('b', 8, new Rook(board, Colors.Black));
        placeNewPiece('a', 8, new King(board, Colors.Black));


    }

}
