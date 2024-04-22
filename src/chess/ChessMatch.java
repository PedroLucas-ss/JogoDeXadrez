package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.awt.*;
import java.security.InvalidParameterException;
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
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
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
    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }
    public ChessPiece getPromoted() {
        return promoted;
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
        ChessPiece movedPiece = (ChessPiece)board.piece(target);
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Colors.White && target.getRow() == 0) || (movedPiece.getColor() == Colors.Black && target.getRow() == 7)) {
                promoted = (ChessPiece)board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }
        check =(testCheck(opponent(currentPlayer)))? true : false;
        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;

    }
    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
            throw new InvalidParameterException("Invalid type for promotion");
        }

        Position pos = promoted.getChessPosition().toPositon();
        Piece p = board.removePiece(pos);
        piecesOnTheBord.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBord.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Colors color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
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
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
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
        // #specialmove castling kingside rook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
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
        placeNewPiece('a', 1, new Rook(board, Colors.White));
        placeNewPiece('b', 1, new Knight(board, Colors.White));
        placeNewPiece('c', 1, new Bishop(board, Colors.White));
        placeNewPiece('d', 1, new Queen(board, Colors.White));
        placeNewPiece('e', 1, new King(board, Colors.White, this));
        placeNewPiece('f', 1, new Bishop(board, Colors.White));
        placeNewPiece('g', 1, new Knight(board, Colors.White));
        placeNewPiece('h', 1, new Rook(board, Colors.White));
        placeNewPiece('a', 2, new Pawn(board, Colors.White, this));
        placeNewPiece('b', 2, new Pawn(board, Colors.White, this));
        placeNewPiece('c', 2, new Pawn(board, Colors.White, this));
        placeNewPiece('d', 2, new Pawn(board, Colors.White, this));
        placeNewPiece('e', 2, new Pawn(board, Colors.White, this));
        placeNewPiece('f', 2, new Pawn(board, Colors.White, this));
        placeNewPiece('g', 2, new Pawn(board, Colors.White, this));
        placeNewPiece('h', 2, new Pawn(board, Colors.White, this));

        placeNewPiece('a', 8, new Rook(board, Colors.Black));
        placeNewPiece('b', 8, new Knight(board, Colors.Black));
        placeNewPiece('c', 8, new Bishop(board, Colors.Black));
        placeNewPiece('d', 8, new Queen(board, Colors.Black));
        placeNewPiece('e', 8, new King(board, Colors.Black, this));
        placeNewPiece('f', 8, new Bishop(board, Colors.Black));
        placeNewPiece('g', 8, new Knight(board, Colors.Black));
        placeNewPiece('h', 8, new Rook(board, Colors.Black));
        placeNewPiece('a', 7, new Pawn(board, Colors.Black, this));
        placeNewPiece('b', 7, new Pawn(board, Colors.Black, this));
        placeNewPiece('c', 7, new Pawn(board, Colors.Black, this));
        placeNewPiece('d', 7, new Pawn(board, Colors.Black, this));
        placeNewPiece('e', 7, new Pawn(board, Colors.Black, this));
        placeNewPiece('f', 7, new Pawn(board, Colors.Black, this));
        placeNewPiece('g', 7, new Pawn(board, Colors.Black, this));
        placeNewPiece('h', 7, new Pawn(board, Colors.Black, this));


    }

}
