package pl.kdd.chesswebapi.chessboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessboardService {
    @Autowired
    private Chessboard chessboard;
    @Autowired
    private ObjectMapper objectMapper;

    public String getChessboard() throws JsonProcessingException {
        String ChessbordAsString = objectMapper.writeValueAsString(chessboard.getBoard());
        return ChessbordAsString;
    }

    public void move(int fromX, int fromY, int toX, int toY) {
        chessboard.move(fromX, fromY, toX, toY, false);
        chessboard.display();
    }
}
