package pl.kdd.chesswebapi.chessboard;

import org.springframework.stereotype.Controller;
import pl.kdd.chesswebapi.chessboard.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Controller
public class Chessboard {
    private Piece[][] board = new Piece[8][8];
    private boolean whitesTurn = true;
    private Pawn[][] enPassantBoard = new Pawn[8][8];

    private final ImageIcon whiteRook = new ImageIcon("src/images/whiteRook.png");
    private final ImageIcon whiteKnight = new ImageIcon("src/images/whiteKnight.png");
    private final ImageIcon whiteBishop = new ImageIcon("src/images/whiteBishop.png");
    private final ImageIcon whiteQueen = new ImageIcon("src/images/whiteQueen.png");

    private final ImageIcon blackRook = new ImageIcon("src/images/blackRook.png");
    private final ImageIcon blackKnight = new ImageIcon("src/images/blackKnight.png");
    private final ImageIcon blackBishop = new ImageIcon("src/images/blackBishop.png");
    private final ImageIcon blackQueen = new ImageIcon("src/images/blackQueen.png");
    
    public Chessboard(){
        //white pieces
        board[0][0] = new Rook(true, this);
        board[0][1] = new Knight(true, this);
        board[0][2] = new Bishop(true, this);
        board[0][3] = new Queen(true, this);
        board[0][4] = new King(true, this);
        board[0][5] = new Bishop(true, this);
        board[0][6] = new Knight(true, this);
        board[0][7] = new Rook(true, this);

        //board[1][0] = new Pawn(true, this);

        for(int i=0; i<8; i++){
            board[1][i] = new Pawn(true, this);
        }

        //black pieces
        board[7][0] = new Rook(false, this);
        board[7][1] = new Knight(false, this);
        board[7][2] = new Bishop(false, this);
        board[7][3] = new Queen(false, this);
        board[7][4] = new King(false, this);
        board[7][5] = new Bishop(false, this);
        board[7][6] = new Knight(false, this);
        board[7][7] = new Rook(false, this);

        //board[3][3] = new Pawn(false, this);

        for(int i=0; i<8; i++){
            board[6][i] = new Pawn(false, this);
        }

    }

    public Chessboard(Chessboard other){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(other.getBoard()[i][j] != null){
                    boolean isWhite = other.getBoard()[i][j].getIsWhite();
                    switch (other.getBoard()[i][j].getTypeID()){
                        case 1 -> {
                            this.board[i][j] = new Pawn(isWhite, this);
                            if(((Pawn)other.getBoard()[i][j]).getHasMoved()){
                                ((Pawn)this.board[i][j]).moved();
                            }
                        }
                        case 2 -> {
                            this.board[i][j] = new Rook(isWhite, this);
                            if(((Rook)other.getBoard()[i][j]).getHasMoved()){
                                ((Rook)this.board[i][j]).moved();
                            }
                        }
                        case 3 -> this.board[i][j] = new Knight(isWhite, this);
                        case 4 -> this.board[i][j] = new Bishop(isWhite, this);
                        case 5 -> this.board[i][j] = new Queen(isWhite, this);
                        case 6 -> {
                            this.board[i][j] = new King(isWhite, this);
                            if(((King)other.getBoard()[i][j]).getHasMoved()){
                                ((King)this.board[i][j]).moved();
                            }
                        }
                    }
                }
                if(other.getEnPassantBoard()[i][j] != null){
                    boolean isWhite = other.getEnPassantBoard()[i][j].getIsWhite();
                    this.enPassantBoard[i][j] = new Pawn(isWhite, this);
                }
            }
        }
        this.whitesTurn = other.whitesTurn;
    }

    public void display(){
        for(int i=7; i>=0; i--){
            for(int j=0; j<8; j++){
                if(board[i][j] != null){
                    System.out.print(board[i][j].getTypeID());
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public Piece[][] getBoard(){
        return board;
    }

    public Piece[][] getEnPassantBoard(){
        return enPassantBoard;
    }

    public int pieceAt(int x, int y){
        //if empty return 0
        if(board[x][y] == null){
            return 0;
        }
        else{
            return board[x][y].getTypeID();
        }
    }

    public boolean pieceAtIsWhite(int x, int y){
        //if empty return false and an information prompt
        if(board[x][y] == null){
            //System.out.println("pieceAtIsWhite(): There is no piece at the specified coordinates, returned false");
            return false;
        }
        else{
            return board[x][y].getIsWhite();
        }
    }

    public boolean isOccupied(int x, int y){
        if(board[x][y] == null){
            return false;
        }
        else{
            return true;
        }
    }

    public void move(int fromX, int fromY, int toX, int toY, boolean isPlayedByEngine){
        Piece tempPiece = null;
        Pawn tempEnPassantPawn = null;

        if(board[fromX][fromY] == null){
            System.out.println("move(): No piece there");
            return;
        }
        
        if(whitesTurn == board[fromX][fromY].getIsWhite() && board[fromX][fromY].isMoveValid(fromX, fromY, toX, toY)){

            //if(board[fromX][fromY].tellType() == 1 && (fromX == 7 || fromX == 0)){
                //pawnPromotion(whitesTurn, fromY);
            //}

            tempPiece = board[toX][toY];
            board[toX][toY] = board[fromX][fromY];
            board[fromX][fromY] = null;

            //test en passant check
            if(enPassantBoard[toX][toY] != null){
                if(whitesTurn){
                    tempEnPassantPawn = (Pawn)board[toX-1][toY];
                    board[toX-1][toY] = null;
                }
                else{
                    tempEnPassantPawn = (Pawn)board[toX+1][toY];
                    board[toX+1][toY] = null;
                }
            }
            //test en passant check

            if(checkIfCheck(whitesTurn)){
                board[fromX][fromY] = board[toX][toY];
                board[toX][toY] = tempPiece;
                System.out.println("move(): Move invalid (king would be under attack)");

                //test en passant check
                if(enPassantBoard[toX][toY] != null){
                    if(whitesTurn){
                        board[toX-1][toY] = tempEnPassantPawn;
                    }
                    else{
                        board[toX+1][toY] = tempEnPassantPawn;
                    }
                }
                //test en passant check

                return;
            }

            if(board[toX][toY].getTypeID() != 1){
                clearEnPassantBoard();
            }

            if(board[toX][toY].getTypeID() == 1){
                Pawn tempPawn = (Pawn)board[toX][toY];

                if(toX - fromX == 2 && whitesTurn){
                    enPassantBoard[fromX+1][fromY] = tempPawn;
                }
                if(toX - fromX == -2 && !whitesTurn){
                    enPassantBoard[fromX-1][fromY] = tempPawn;
                }

                //unnecessary, the pawn is deleted by check checking anyway

                //if(enPassantBoard[toX][toY] != null){
                //    for(int i=0; i<8; i++){
                //        for(int j=0; j<8; j++){
                //            if(board[i][j] == enPassantBoard[toX][toY]){
                //                //board[i][j] = null;
                //                clearEnPassantBoard();
                //            }
                //        }
                //    }
                //}

                tempPawn.moved();

                
                if(toX == 7 || toX == 0){
                    pawnPromotion(whitesTurn, toY);
                }
                
            }
            if(board[toX][toY].getTypeID() == 2){
                Rook tempRook = (Rook)board[toX][toY];
                tempRook.moved();
            }
            if(board[toX][toY].getTypeID() == 6){
                King tempKing = (King)board[toX][toY];

                if(fromY - toY == 2){
                    //long castle
                    for(int i=fromY-1; i>=0; i--){
                        if(board[fromX][i] != null && board[fromX][i].getTypeID() == 2){
                            board[fromX][fromY-1] = board[fromX][i];
                            board[fromX][i] = null;
                        }
                    }
                }

                if(fromY - toY == -2){
                    //short castle
                    for(int i=fromY+1; i<8; i++){
                        if(board[fromX][i] != null && board[fromX][i].getTypeID() == 2){
                            board[fromX][fromY+1] = board[fromX][i];
                            board[fromX][i] = null;
                        }
                    }
                }

                tempKing.moved();
            }

            //System.out.println("move(): fromX: "+fromX+" fromY: "+fromY+" toX: "+toX+" toY: "+toY+"  The move has been made");
            whitesTurn = !whitesTurn;
        }
        else{
            //System.out.println("move(): fromX: "+fromX+" fromY: "+fromY+" toX: "+toX+" toY: "+toY+"  The move is invalid");
        }

    }

    public boolean isMovePossible(int fromX, int fromY, int toX, int toY){
        Piece tempPiece = null;
        Pawn tempEnPassantPawn = null;

        if(board[fromX][fromY] == null){
            //System.out.println("isMovePossible(): No piece there");
            return false;
        }

        if(board[fromX][fromY].isMoveValid(fromX, fromY, toX, toY)){
            tempPiece = board[toX][toY];
            board[toX][toY] = board[fromX][fromY];
            board[fromX][fromY] = null;

            //test en passant check
            if(enPassantBoard[toX][toY] != null){
                if(whitesTurn){
                    tempEnPassantPawn = (Pawn)board[toX-1][toY];
                    board[toX-1][toY] = null;
                }
                else{
                    tempEnPassantPawn = (Pawn)board[toX+1][toY];
                    board[toX+1][toY] = null;
               }
            }
            //test en passant check

            if(checkIfCheck(whitesTurn)){
                board[fromX][fromY] = board[toX][toY];
                board[toX][toY] = tempPiece;

                //test en passant check
                if(enPassantBoard[toX][toY] != null){
                    if(whitesTurn){
                        board[toX-1][toY] = tempEnPassantPawn;
                    }
                    else{
                        board[toX+1][toY] = tempEnPassantPawn;
                    }
                }
                //test en passant check

                return false;
            }
            
                board[fromX][fromY] = board[toX][toY];
                board[toX][toY] = tempPiece;

                //test en passant check
                if(enPassantBoard[toX][toY] != null){
                    if(whitesTurn){
                        board[toX-1][toY] = tempEnPassantPawn;
                    }
                    else{
                        board[toX+1][toY] = tempEnPassantPawn;
                    }
                }
                //test en passant check

                return true;

        }

        else{
            return false;
        }


    }

    public boolean isThatCurrentPlayer(int x, int y){
        if(board[x][y].getIsWhite() == whitesTurn){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkIfCheck(boolean isKingWhite){
        //System.out.println("checkIfCheck(): Invoked");
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){

                if(board[i][j] != null){
                    if(board[i][j].getTypeID() == 6 && board[i][j].getIsWhite() == isKingWhite){

                        return checkIfCheck(i, j ,isKingWhite);
                    }
                }
            }
        }
        return false;
    }

    public boolean checkIfCheck(int i, int j, boolean isKingWhite){

                //better rook version (y+)
                for(int k=1; k<8; k++){
                    int y = j + k;
                    if(y>7){
                        break;
                    }

                    if(board[i][y] != null){
                        if((board[i][y].getTypeID() == 2 || board[i][y].getTypeID() == 5) && board[i][y].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }

                //(y-)
                for(int k=1; k<8; k++){
                    int y = j - k;
                    if(y<0){
                        break;
                    }

                    if(board[i][y] != null){
                        if((board[i][y].getTypeID() == 2 || board[i][y].getTypeID() == 5) && board[i][y].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }

                //(x+)
                for(int k=1; k<8; k++){
                    int x = i + k;
                    if(x>7){
                        break;
                    }

                    if(board[x][j] != null){
                        if((board[x][j].getTypeID() == 2 || board[x][j].getTypeID() == 5) && board[x][j].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }

                //(x-)
                for(int k=1; k<8; k++){
                    int x = i - k;
                    if(x<0){
                        break;
                    }

                    if(board[x][j] != null){
                        if((board[x][j].getTypeID() == 2 || board[x][j].getTypeID() == 5) && board[x][j].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }
                
                //better bishop version (++)
                for(int k=1; k<8; k++){
                    int x = i + k;
                    int y = j + k;

                    if(x>7 || y>7){
                        break;
                    }

                    if(board[x][y] != null){
                        if((board[x][y].getTypeID() == 4 || board[x][y].getTypeID() == 5) && board[x][y].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }

                //(+-)
                for(int k=1; k<8; k++){
                    int x = i + k;
                    int y = j - k;

                    if(x>7 || y<0){
                        break;
                    }

                    if(board[x][y] != null){
                        if((board[x][y].getTypeID() == 4 || board[x][y].getTypeID() == 5) && board[x][y].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }

                //(-+)
                for(int k=1; k<8; k++){
                    int x = i - k;
                    int y = j + k;

                    if(x<0 || y>7){
                        break;
                    }

                    if(board[x][y] != null){
                        if((board[x][y].getTypeID() == 4 || board[x][y].getTypeID() == 5) && board[x][y].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }

                //(--)
                for(int k=1; k<8; k++){
                    int x = i - k;
                    int y = j - k;

                    if(x<0 || y<0){
                        break;
                    }

                    if(board[x][y] != null){
                        if((board[x][y].getTypeID() == 4 || board[x][y].getTypeID() == 5) && board[x][y].getIsWhite() != isKingWhite){
                            return true;
                        }
                        else{
                            break;
                        }
                    }
                }

                //better knight version
                int[] knightXArray = {1, 2, 1, 2, -1, -2, -1, -2};
                int[] knightYArray = {2, 1, -2, -1, 2, 1, -2, -1};

                for(int k=0; k<8; k++){
                    int x = i + knightXArray[k];
                    int y = j + knightYArray[k];

                    if(x>=0 && x<8 && y>=0 && y<8){
                        if(board[x][y] != null && board[x][y].getTypeID() == 3 && board[x][y].getIsWhite() != isKingWhite){
                            return true;
                        }
                    }

                }

                //check by a pawn
                //king at [i][j]
                if(isKingWhite){
                    if(i+1<8 && j+1<8 && board[i+1][j+1] != null && board[i+1][j+1].getTypeID() == 1 && board[i+1][j+1].getIsWhite() != isKingWhite){
                        return true;
                    }
                    if(i+1<8 && j-1>=0 && board[i+1][j-1] != null && board[i+1][j-1].getTypeID() == 1 && board[i+1][j-1].getIsWhite() != isKingWhite){
                        return true;
                    }
                }
                else{
                    if(i-1>=0 && j+1<8 && board[i-1][j+1] != null && board[i-1][j+1].getTypeID() == 1 && board[i-1][j+1].getIsWhite() != isKingWhite){
                        return true;
                    }
                    if(i-1>=0 && j-1>=0 && board[i-1][j-1] != null && board[i-1][j-1].getTypeID() == 1 && board[i-1][j-1].getIsWhite() != isKingWhite){
                        return true;
                    }
                }

                //"check" by a king
                int[] kingXArray = {1, 1, 1, 0, 0, -1, -1, -1};
                int[] kingYArray = {1, 0, -1, 1, -1, 1, 0, -1};

                for(int k=0; k<8; k++){
                    int x = i + kingXArray[k];
                    int y = j + kingYArray[k];

                    if(x>=0 && x<8 && y>=0 && y<8){
                        if(board[x][y] != null && board[x][y].getTypeID() == 6 && board[x][y].getIsWhite() != isKingWhite){
                            //System.out.println("King would be checked by another king");
                            return true;
                        }
                    }
                }



        return false;

    }

    public void printEnPassantBoard(){
        System.out.println("En Passant board looks like:");
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(enPassantBoard[i][j] != null){
                    System.out.println("En Passant pawn at x: "+i+" y: "+j);
                }
            }
        }
    }

    public boolean isEnPassantPossible(int fromX, int fromY, int toX, int toY){
        if(enPassantBoard[toX][toY] != null){
            if(board[fromX][fromY].getIsWhite() != enPassantBoard[toX][toY].getIsWhite()){
                //System.out.println("enPassantPossible(): Returned true");
                return true;
            }
            else{
                return false;
            }

        }
        else{
            //System.out.println("enPassantPossible(): Returned false. Values x: "+toX+" y: "+toY);
            return false;
        }
    }

    private void clearEnPassantBoard(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(enPassantBoard[i][j] != null){
                    enPassantBoard[i][j] = null;
                }
            }
        }
    }

    private void pawnPromotion(boolean isPawnWhite, int y){
        //App.selectLock = true;
        int x;
        if(isPawnWhite){
            x = 7;
        }
        else{
            x = 0;
        }

        System.out.println("PAWN PROMOTION isPawnWhite: "+isPawnWhite+"x: "+x+"y: "+y);
        JFrame promotionFrame = new JFrame();
        JPanel promotionPanel = new JPanel();
        JLabel[] promotionLabel = new JLabel[4];

        promotionPanel.setLayout(new GridLayout(4, 1));

        for(int i=0; i<4; i++){
            int ii = i;
            promotionLabel[i] = new JLabel(whiteQueen);
            
            promotionLabel[i].addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e){
                    promote(x, y, isPawnWhite, ii);
                    promotionFrame.dispose();
                }
            });

            promotionPanel.add(promotionLabel[i]);
        }

        if(isPawnWhite){
            promotionLabel[0].setIcon(whiteQueen);
            promotionLabel[1].setIcon(whiteRook);
            promotionLabel[2].setIcon(whiteBishop);
            promotionLabel[3].setIcon(whiteKnight);
        }
        else{
            promotionLabel[0].setIcon(blackQueen);
            promotionLabel[1].setIcon(blackRook);
            promotionLabel[2].setIcon(blackBishop);
            promotionLabel[3].setIcon(blackKnight);
        }

        promotionFrame.setTitle("Pawn promotion");
        promotionFrame.add(promotionPanel);
        promotionFrame.pack();
        promotionFrame.setVisible(true);
    }

    private void promote(int x, int y, boolean isPawnWhite, int id){
        if(id<0 || id>3){
            return;
        }

        board[x][y] = null;

        switch(id){
            case 0: board[x][y] = new Queen(isPawnWhite, this);
            break;
            case 1: board[x][y] = new Rook(isPawnWhite, this);
            break;
            case 2: board[x][y] = new Bishop(isPawnWhite, this);
            break;
            case 3: board[x][y] = new Knight(isPawnWhite, this);
        }
        
        //App.selectLock = false;
        //App.iconRefresh();
    }

    public boolean checkIfEnd(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j] != null && board[i][j].getIsWhite() == whitesTurn){
                    for(int k=0; k<8; k++){
                        for(int l=0; l<8; l++){
                            if(isMovePossible(i, j, k, l)){
                                return false;
                            }
                        }
                    }
                }
        }
    }
    if(checkIfCheck(whitesTurn)){
        System.out.println("GAME OVER: CHECKMATE");
    }
    else{
        System.out.println("GAME OVER: STALEMATE");
    }
    return true;
}

}
