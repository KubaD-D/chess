package pl.kdd.chesswebapi.chessboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ChessboardController {
    @Autowired
    private ChessboardService chessboardService;

    @GetMapping("/api/chessboard")
    public ResponseEntity<String> getChessboard() {
        try {
            return ResponseEntity.ok(chessboardService.getChessboard());
        }
        catch(JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PutMapping("/api/move/{fromX}/{fromY}/{toX}/{toY}")
    public void move(@PathVariable("fromX") int fromX, @PathVariable("fromY") int fromY,
                     @PathVariable("toX") int toX, @PathVariable("toY") int toY){
        chessboardService.move(fromX, fromY, toX, toY);

    }
}
