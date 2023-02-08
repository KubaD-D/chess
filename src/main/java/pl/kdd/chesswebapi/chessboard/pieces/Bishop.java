package pl.kdd.chesswebapi.chessboard.pieces;

import pl.kdd.chesswebapi.chessboard.Chessboard;

public class Bishop extends Piece {
    
    public Bishop(boolean b, Chessboard board){
        isWhite = b;
        atBoard = board;
    }

    public int getTypeID(){
        return 4;
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    public boolean isMoveValid(int fromX, int fromY, int toX, int toY){

        if(!atBoard.isOccupied(toX, toY) || atBoard.isOccupied(toX, toY) && atBoard.pieceAtIsWhite(toX, toY) != isWhite){

        if(Math.abs(toX - fromX) == Math.abs(toY - fromY) && fromX != toX && fromY != toY){
            
            if(toX > fromX){
                if(toY > fromY){
                    for(int i=1; i<Math.abs(toX - fromX); i++){
                        if(atBoard.isOccupied(toX-i, toY-i)){
                            return false;
                        }
                    }
                }
                else{
                    for(int i=1; i<Math.abs(toX - fromX); i++){
                        if(atBoard.isOccupied(toX-i, toY+i)){
                            return false;
                        }
                    }
                }
            }else if(toY > fromY){
                for(int i=1; i<Math.abs(toX - fromX); i++){
                    if(atBoard.isOccupied(toX+i, toY-i)){
                        return false;
                    }
                }
            }
            else{
                for(int i=1; i<Math.abs(toX - fromX); i++){
                    if(atBoard.isOccupied(toX+i, toY+i)){
                        return false;
                    }
                }
            }

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
