package boardgame;

public class Board {

    private Integer rows;
    private Integer columns;
    private Piece[][] pieces;

    public Board(Integer rows, Integer columns) {
        if ( rows < 1 || columns < 1){
            throw new BoardException("Erro ao criar o tabuleiro, e necessario que tenha ao menos uma linha ou uma coluna");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }
    public Piece piece(int row, int column){
        if(!positionExists(row, column)){
            throw new BoardException("Posicao fora do tabuleiro");
        }
        return pieces[row][column];
    }
    public Piece piece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Posicao fora do tabuleiro");
        }
        return pieces[position.getRow()][position.getColumn()];
    }
    public void placePiece(Piece piece, Position position){
        if(thereIsApiece(position)){
            throw new BoardException("Ja existe uma peca nessa possicao "+ position);
        }

        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;

    }
    private boolean positionExists(int row, int column){
        return row >= 0 && row < rows && columns >= 0 && column < columns;
    }
    public boolean positionExists(Position position){

        return positionExists(position.getRow(), position.getColumn());

    }
    public boolean thereIsApiece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Posicao fora do tabuleiro");
        }
        return piece(position) != null;

    }
}
