package pl.kdd.chesswebapi.chessboard.pieces;

import pl.kdd.chesswebapi.chessboard.Chessboard;

public class King extends Piece {
    private boolean hasMoved = false;
    
    public King(boolean b, Chessboard board){
        isWhite = b;
        atBoard = board;
    }

    public int getTypeID(){
        return 6;
    }

    public boolean getIsWhite(){
        return isWhite;
    }

    public boolean isMoveValid(int fromX, int fromY, int toX, int toY){

        if(toY - fromY == -2 && fromX == toX && !hasMoved){
            Rook tempRook = null;
            //castle
            //rook on the left
            for(int i=fromY-1; i>=0; i--){
                if(atBoard.getBoard()[fromX][i] != null && atBoard.getBoard()[fromX][i].getTypeID() == 2 && atBoard.pieceAtIsWhite(fromX, i) == isWhite){
                    tempRook = (Rook)atBoard.getBoard()[fromX][i];
                    if(!tempRook.getHasMoved()){
                        for(int j=i+1; j<fromY; j++){
                            if(atBoard.isOccupied(fromX, j)){
                                return false;
                            }
                        }

                        //maybe you have to check if those fromYs are not null but come on
                        if(!atBoard.checkIfCheck(fromX, fromY, isWhite) && !atBoard.checkIfCheck(fromX, fromY-1, isWhite) && !atBoard.checkIfCheck(fromX, fromY-2, isWhite)){
                            System.out.println("left castling allowed");
                            return true;
                        }
                        else{
                            System.out.println("left castling not allowed");
                            return false;
                        }
                    }
                    else{
                        return false;
                    }

                }
            }
            return false;
        }

            if(toY - fromY == 2 && fromX == toX && !hasMoved){
                Rook tempRook = null;
            //rook on the right
            for(int i=fromY+1; i<8; i++){
                if(atBoard.getBoard()[fromX][i] != null && atBoard.getBoard()[fromX][i].getTypeID() == 2 && atBoard.pieceAtIsWhite(fromX, i) == isWhite){
                    tempRook = (Rook)atBoard.getBoard()[fromX][i];
                    if(!tempRook.getHasMoved()){
                        for(int j=i-1; j>fromY; j--){
                            if(atBoard.isOccupied(fromX, j)){
                                return false;
                            }
                        }
                        if(!atBoard.checkIfCheck(fromX, fromY, isWhite) && !atBoard.checkIfCheck(fromX, fromY+1, isWhite) && !atBoard.checkIfCheck(fromX, fromY+2, isWhite)){
                            return true;
                            //return false;
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
            return false;
        }
            


        if(!atBoard.isOccupied(toX, toY) || atBoard.isOccupied(toX, toY) && atBoard.pieceAtIsWhite(toX, toY) != isWhite){
            if(Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 1 || Math.abs(toX - fromX) == 0 && Math.abs(toY - fromY) == 1 ||
             Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 0){
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
    public boolean getHasMoved(){return hasMoved;}

}
