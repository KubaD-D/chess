package pl.kdd.chesswebapi.chessboard.pieces;

import pl.kdd.chesswebapi.chessboard.Chessboard;

public class Queen extends Piece {
    
    public Queen(boolean b, Chessboard board){
        isWhite = b;
        atBoard = board;
    }

    public int getTypeID(){
        return 5;
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    public boolean isMoveValid(int fromX, int fromY, int toX, int toY){
        
        Rook tempRook = new Rook(isWhite, atBoard);
        Bishop tempBishop = new Bishop(isWhite, atBoard);

        if(tempRook.isMoveValid(fromX, fromY, toX, toY) || tempBishop.isMoveValid(fromX, fromY, toX, toY)){
            return true;
        }
        else{
            return false;
        }

    }
}
