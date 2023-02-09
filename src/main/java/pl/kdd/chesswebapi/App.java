package pl.kdd.chesswebapi;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import pl.kdd.chesswebapi.chessboard.*;
//test

public class App{

        static ImageIcon whitePawn = new ImageIcon("src/images/whitePawn.png");
        static ImageIcon whiteRook = new ImageIcon("src/images/whiteRook.png");
        static ImageIcon whiteKnight = new ImageIcon("src/images/whiteKnight.png");
        static ImageIcon whiteBishop = new ImageIcon("src/images/whiteBishop.png");
        static ImageIcon whiteQueen = new ImageIcon("src/images/whiteQueen.png");
        static ImageIcon whiteKing = new ImageIcon("src/images/whiteKing.png");

        static ImageIcon blackPawn = new ImageIcon("src/images/blackPawn.png");
        static ImageIcon blackRook = new ImageIcon("src/images/blackRook.png");
        static ImageIcon blackKnight = new ImageIcon("src/images/blackKnight.png");
        static ImageIcon blackBishop = new ImageIcon("src/images/blackBishop.png");
        static ImageIcon blackQueen = new ImageIcon("src/images/blackQueen.png");
        static ImageIcon blackKing = new ImageIcon("src/images/blackKing.png");

        static Chessboard b = new Chessboard();
        static JFrame frame = new JFrame();
        static JPanel boardPanel = new JPanel();
        static JLabel[][] boardLabel = new JLabel[8][8];

        static boolean isAnyPieceSelected = false;
        static boolean[][] isSelected = new boolean[8][8];

        static boolean selectLock = false;

        

    public static void main(String[] args){

        boardPanel.setLayout(new GridLayout(8, 8));

        //ImageIcon empty = new ImageIcon("src/images/empty.png");

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                isSelected[i][j] = false;
            }
        }


        for(int i=7; i>=0; i--){
            int ii = i;
            for(int j=0; j<8; j++){
                int jj = j;
                boardLabel[i][j] = new JLabel();
                boardLabel[i][j].setOpaque(true);
                boardLabel[i][j].setIcon(whiteQueen);

                 boardLabel[i][j].addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        squareClicked(ii, jj);
                    }
                 }
                 
                 );

            boardPanel.add(boardLabel[i][j]);
        }
    }

        frame.setTitle("Chess");
        frame.add(boardPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        iconRefresh();
    }

    
    
    public static void iconRefresh(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(b.pieceAtIsWhite(i, j)){

                    switch(b.pieceAt(i, j)){
                        case 1: boardLabel[i][j].setIcon(whitePawn);
                        break;
                        case 2: boardLabel[i][j].setIcon(whiteRook);
                        break;
                        case 3: boardLabel[i][j].setIcon(whiteKnight);
                        break;
                        case 4: boardLabel[i][j].setIcon(whiteBishop);
                        break;
                        case 5: boardLabel[i][j].setIcon(whiteQueen);
                        break;
                        case 6: boardLabel[i][j].setIcon(whiteKing);
                        break;
                        default: boardLabel[i][j].setIcon(null);
                    }
                }
                else
                {
                    switch(b.pieceAt(i, j)){
                        case 1: boardLabel[i][j].setIcon(blackPawn);
                        break;
                        case 2: boardLabel[i][j].setIcon(blackRook);
                        break;
                        case 3: boardLabel[i][j].setIcon(blackKnight);
                        break;
                        case 4: boardLabel[i][j].setIcon(blackBishop);
                        break;
                        case 5: boardLabel[i][j].setIcon(blackQueen);
                        break;
                        case 6: boardLabel[i][j].setIcon(blackKing);
                        break;
                        default: boardLabel[i][j].setIcon(null);
                }
            }
            if((i+j)%2 == 0){
                boardLabel[i][j].setBackground(Color.BLUE);
            }
            else{
                boardLabel[i][j].setBackground(Color.WHITE);
            }
        }
    }}

    public static void markValidMoves(int x, int y){
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                //if( b.getBoard()[x][y].isMoveValid(x, y, i, j) ){
                    if( b.isMovePossible(x, y, i, j) ){
                    if((i+j)%2 == 0){
                    boardLabel[i][j].setBackground(new Color(255,0,0));
                    }
                    else{
                        boardLabel[i][j].setBackground(new Color(200,0,0));
                    }
                }
            }
        }

    }

    public static void squareClicked(int x, int y){
        if(!selectLock){
            if(isAnyPieceSelected){
                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        if(isSelected[i][j]){
                            boolean isEngineAllowedToPlay = b.isMovePossible(i, j, x, y);
                            b.move(i, j, x, y, false);
                            isSelected[i][j] = false;
                            isAnyPieceSelected = false;
                            //b.printEnPassantBoard();
                            iconRefresh();

                            if(isEngineAllowedToPlay){
                                int[] bestMove = Engine.findBestMove(b, false, 2);
                                System.out.println("The best move is: "+bestMove[0]+" "+bestMove[1]+" "+bestMove[2]+" "+bestMove[3]);
                                b.move(bestMove[0], bestMove[1], bestMove[2], bestMove[3], false);
                                System.out.println(Engine.evaluateChessboard(b));
                                b.move(7, 4, 7, 6, true);
                            }


                            iconRefresh();
                            b.checkIfEnd();

                        }
                    }
                }
            }
            else if(b.isOccupied(x, y)){
                if(b.isThatCurrentPlayer(x, y)){
                    boardLabel[x][y].setBackground(Color.GREEN);
                    markValidMoves(x, y);
                    isSelected[x][y] = true;
                    isAnyPieceSelected = true;
                }
                else{
                    System.out.println("squareClicked(): Wrong piece color");
                }
            }
            else{
                System.out.println("squareClicked(): Empty square");
            }
        }
        else{
            System.out.println("squareClicked(): Select locked");
        }
    }
    


}
