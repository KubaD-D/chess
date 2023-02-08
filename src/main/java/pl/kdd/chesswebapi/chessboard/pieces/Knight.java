package pl.kdd.chesswebapi.chessboard.pieces;

import pl.kdd.chesswebapi.chessboard.Chessboard;

public class Knight extends Piece {
    
    public Knight(boolean b, Chessboard board){
        isWhite = b;
        atBoard = board;
    }

    public int getTypeID(){
        return 3;
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    public boolean isMoveValid(int fromX, int fromY, int toX, int toY){

        if(!atBoard.isOccupied(toX, toY) || atBoard.isOccupied(toX, toY) && atBoard.pieceAtIsWhite(toX, toY) != isWhite){

            if(Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 2 || Math.abs(toX - fromX) == 2 && Math.abs(toY - fromY) == 1){
                return true;
            }
            else{
                return false;
            }

        }
        else{
            return false;
        }
    }
}
