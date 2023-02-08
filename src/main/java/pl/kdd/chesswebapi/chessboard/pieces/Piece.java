package pl.kdd.chesswebapi.chessboard.pieces;

import pl.kdd.chesswebapi.chessboard.Chessboard;

public abstract class Piece {
    protected boolean isWhite;
    protected Chessboard atBoard;
    
    public abstract int getTypeID();
    public abstract boolean getIsWhite();
    public abstract boolean isMoveValid(int fromX, int fromY, int toX, int toY);

}
