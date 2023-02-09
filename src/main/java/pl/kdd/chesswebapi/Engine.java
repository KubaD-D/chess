package pl.kdd.chesswebapi;
import pl.kdd.chesswebapi.chessboard.*;

public class Engine {

    public static void main(String[] args){
        Chessboard chessboard = new Chessboard();

        chessboard.move(1, 4, 3, 4, false);
        chessboard.move(6, 4, 5, 4, false);
        chessboard.move(1, 3, 3, 3, false);

        int[] bestMove = findBestMove(chessboard, false, 4);
        System.out.println(bestMove[0]);
        System.out.println(bestMove[1]);
        System.out.println(bestMove[2]);
        System.out.println(bestMove[3]);

        //chessboard.move(1, 0, 3, 0);

    }

    public static int[] findBestMove(Chessboard chessboard, boolean isWhite, int depth){

        Chessboard chessboardCopy = new Chessboard(chessboard);
        int[] bestMove = new int[4];
        double bestEval;
        if(isWhite){
            bestEval = -1000;
        }
        else{
            bestEval = 1000;
        }

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(chessboard.getBoard()[i][j] != null && chessboard.getBoard()[i][j].getIsWhite() == isWhite){
                    for(int k=0; k<8; k++){
                        for(int l=0; l<8; l++){
                            if(chessboard.isMovePossible(i, j, k, l)){
                                chessboardCopy.move(i, j, k, l, true);
                                double eval = minmax(chessboardCopy, !isWhite, depth-1);
                                if(isWhite && eval >= bestEval){
                                    bestEval = eval;
                                    bestMove[0] = i;
                                    bestMove[1] = j;
                                    bestMove[2] = k;
                                    bestMove[3] = l;
                                }
                                else if(!isWhite && eval <= bestEval){
                                    bestEval = eval;
                                    bestMove[0] = i;
                                    bestMove[1] = j;
                                    bestMove[2] = k;
                                    bestMove[3] = l;
                                }
                                chessboardCopy = new Chessboard(chessboard);
                            }
                        }
                    }
                }
            }
        }
        return bestMove;
    }

    private static double minmax(Chessboard chessboard, boolean isWhite, int depth){

        if(depth == 0){
            return evaluateChessboard(chessboard);
        }

        double bestResult;
        Chessboard chessboardCopy = new Chessboard(chessboard);
        if(isWhite){
            bestResult = -1000;
        }
        else{
            bestResult = 1000;
        }

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(chessboard.getBoard()[i][j] != null && chessboard.getBoard()[i][j].getIsWhite() == isWhite){
                    //znaleziony pionek obecnego gracza
                    for(int k=0; k<8; k++){
                        for(int l=0; l<8; l++){
                            if(chessboardCopy.isMovePossible(i, j, k, l)){

                                chessboardCopy.move(i, j, k, l, true);
                                double result = minmax(chessboardCopy, false, depth-1);
                                if(isWhite && result > bestResult){
                                        bestResult = result;
                                }
                                else if(!isWhite && result < bestResult){
                                    bestResult = result;
                                }
                                chessboardCopy = new Chessboard(chessboard);
                            }
                        }
                    }
                }
            }
        }
        return bestResult;
    }

    public static double evaluateChessboard(Chessboard chessboard){

        double whiteEvaluation = 0.0;
        double blackEvaluation = 0.0;


        if(chessboard.checkIfEnd()){
            if(chessboard.checkIfCheck(true)){
                return -100;
            }
            else if(chessboard.checkIfCheck(false)){
                return 100;
            }
            else{
                return 0;
            }
        }

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(chessboard.getBoard()[i][j] != null){
                    if(chessboard.getBoard()[i][j].getIsWhite()){
                        switch (chessboard.getBoard()[i][j].getTypeID()) {
                            case 1 -> {
                                whiteEvaluation += 1;
                                if(isPawnDoubled(chessboard, i, j)){whiteEvaluation -= 0.5;}
                                if(isPawnBlocked(chessboard, i, j)){whiteEvaluation -= 0.5;}
                                if(isPawnIsolated(chessboard, i, j)){whiteEvaluation -= 0.5;}
                            }
                            case 2 -> whiteEvaluation += 5;
                            case 3, 4 -> whiteEvaluation += 3;
                            case 5 -> whiteEvaluation += 9;
                        }
                    }
                    else{
                            switch (chessboard.getBoard()[i][j].getTypeID()) {
                                case 1 -> {
                                    blackEvaluation += 1;
                                    if(isPawnDoubled(chessboard, i, j)){blackEvaluation -= 0.5;}
                                    if(isPawnBlocked(chessboard, i, j)){blackEvaluation -= 0.5;}
                                    if(isPawnIsolated(chessboard, i, j)){blackEvaluation -= 0.5;}
                                }
                                case 2 -> blackEvaluation += 5;
                                case 3, 4 -> blackEvaluation += 3;
                                case 5 -> blackEvaluation += 9;
                        }

                    }
                }
            }
        }

        whiteEvaluation += 0.1 * numberOfPossibleMoves(chessboard, true);
        blackEvaluation += 0.1 * numberOfPossibleMoves(chessboard, false);
        //System.out.println("White evaluation: "+whiteEvaluation);
        //System.out.println("Black evaluation: "+blackEvaluation);
        //System.out.println("Overall evaluation: "+(whiteEvaluation-blackEvaluation));
        return whiteEvaluation - blackEvaluation;
    }

    private static int numberOfPossibleMoves(Chessboard chessboard, boolean isWhite){
        int possibleMoves = 0;

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){

                if(chessboard.getBoard()[i][j] != null && chessboard.getBoard()[i][j].getIsWhite() == isWhite){
                    for(int k=0; k<8; k++){
                        for(int l=0; l<8; l++){
                                if (chessboard.isMovePossible(i, j, k, l)) {
                                    possibleMoves += 1;
                                }
                            }
                        }
                    }
            }
        }

        return possibleMoves;
    }

    private static boolean isPawnDoubled(Chessboard chessboard, int pawnX, int pawnY){
        boolean isWhite = chessboard.getBoard()[pawnX][pawnY].getIsWhite();

        if(chessboard.getBoard()[pawnX][pawnY].getIsWhite()){
            for(int i=pawnX+1; i<8; i++){
                if(chessboard.getBoard()[i][pawnY] != null && chessboard.getBoard()[i][pawnY].getTypeID() == 1 && chessboard.getBoard()[i][pawnY].getIsWhite() == isWhite){
                    return true;
                }
            }
        }
        else{
            for(int i=pawnX-1; i>=0; i--){
                if(chessboard.getBoard()[i][pawnY] != null && chessboard.getBoard()[i][pawnY].getTypeID() == 1 && chessboard.getBoard()[i][pawnY].getIsWhite() == isWhite){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isPawnIsolated(Chessboard chessboard, int pawnX, int pawnY) {
        boolean isWhite = chessboard.getBoard()[pawnX][pawnY].getIsWhite();
        boolean leftIsolated = true;
        boolean rightIsolated = true;

        if (pawnY - 1 >= 0) {
            for (int i = 0; i < 8; i++) {
                if (chessboard.getBoard()[i][pawnY - 1] != null && chessboard.getBoard()[i][pawnY - 1].getTypeID() == 1 && chessboard.getBoard()[i][pawnY - 1].getIsWhite() == isWhite) {
                    leftIsolated = false;
                    break;
                }
            }
        }
        if (pawnY + 1 < 8) {
            for (int i = 0; i < 8; i++) {
                if (chessboard.getBoard()[i][pawnY + 1] != null && chessboard.getBoard()[i][pawnY + 1].getTypeID() == 1 && chessboard.getBoard()[i][pawnY + 1].getIsWhite() == isWhite) {
                    rightIsolated = false;
                    break;
                }
            }
        }

        return leftIsolated && rightIsolated;
    }

    private static boolean isPawnBlocked(Chessboard chessboard, int pawnX, int pawnY){
        boolean isWhite = chessboard.getBoard()[pawnX][pawnY].getIsWhite();

        if(isWhite && pawnX+1 < 8){
            if(chessboard.getBoard()[pawnX+1][pawnY] != null && chessboard.getBoard()[pawnX+1][pawnY].getIsWhite() != isWhite){
                return true;
            }
        }
        if(!isWhite && pawnX-1 >= 0){
            if(chessboard.getBoard()[pawnX-1][pawnY] != null && chessboard.getBoard()[pawnX-1][pawnY].getIsWhite() != isWhite){
                return true;
            }
        }
        return false;
    }

}
