package boardgame;

import java.util.Scanner;

public class BoardExcepcion  extends RuntimeException{

    public BoardExcepcion(String msg){
        super(msg);
    }
}
