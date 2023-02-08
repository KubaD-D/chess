package pl.kdd.chesswebapi.chessboard.pieces;

import pl.kdd.chesswebapi.chessboard.Chessboard;

public class Rook extends Piece {
    private boolean hasMoved = false;
    
    public Rook(boolean b, Chessboard board){
        isWhite = b;
        atBoard = board;
    }

    public int getTypeID(){
        return 2;
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    public boolean isMoveValid(int fromX, int fromY, int toX, int toY){

        if(!atBoard.isOccupied(toX, toY) || atBoard.isOccupied(toX, toY) && atBoard.pieceAtIsWhite(toX, toY) != isWhite){

            if(toX == fromX && toY != fromY){
                if(toY > fromY){
                    for(int i=toY-1; i>fromY; i--){
                        if(atBoard.isOccupied(toX, i)){
                            return false;
                        }
                    }
                }
                else{
                    for(int i=toY+1; i<fromY; i++){
                        if(atBoard.isOccupied(toX, i)){
                            return false;
                        }
                    }
                }
                return true;
            }
            else if(toY == fromY && toX != fromX){

                if(toX > fromX){
                    for(int i=toX-1; i>fromX; i--){
                        if(atBoard.isOccupied(i, toY)){
                            return false;
                        }
                    }
                }
                else{
                    for(int i=toX+1; i<fromX; i++){
                        if(atBoard.isOccupied(i, toY)){
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

    public void moved(){
        hasMoved = true;
    }

    public boolean getHasMoved(){
        return hasMoved;
    }

}

