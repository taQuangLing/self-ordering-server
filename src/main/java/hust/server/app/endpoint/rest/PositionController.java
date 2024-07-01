package hust.server.app.endpoint.rest;

import hust.server.app.service.ResponseFactory;
import hust.server.domain.authen.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @GetMapping("/admin/v1/positions")
    public ResponseEntity<?> getPositionList(@RequestParam String userId){
        return ResponseFactory.response(positionService.getPositionList(userId));
    }
}
