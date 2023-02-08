package pl.kdd.chesswebapi.chessboard.pieces;

import pl.kdd.chesswebapi.chessboard.Chessboard;

public class Pawn extends Piece {
    private boolean hasMoved = false;

    public Pawn(boolean b, Chessboard board){
        isWhite = b;
        atBoard = board;
    }

    public int getTypeID(){
        return 1;
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    public boolean isMoveValid(int fromX, int fromY, int toX, int toY){

        if(!atBoard.isOccupied(toX, toY) && !atBoard.isEnPassantPossible(fromX, fromY, toX, toY)){

            if(fromY == toY){
                if(toX - fromX == 1 && isWhite || toX - fromX == -1 && !isWhite){
                    return true;
                }
                else if(!hasMoved){
                    if(toX - fromX == 2 && isWhite && !atBoard.isOccupied(toX-1, toY) || toX - fromX == -2 && !isWhite && !atBoard.isOccupied(toX+1, toY)){
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

            else{
                return false;
            }

        }
        else if(atBoard.pieceAtIsWhite(toX, toY) != isWhite || atBoard.isEnPassantPossible(fromX, fromY, toX, toY)){

            if(toX - fromX == 1 && isWhite || toX - fromX == -1 && !isWhite){
                if(Math.abs(toY - fromY) == 1){
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
        else{
            return false;
        }

    }

    public void moved(){
        hasMoved = true;
    }
    public boolean getHasMoved(){return hasMoved;}
    
}

