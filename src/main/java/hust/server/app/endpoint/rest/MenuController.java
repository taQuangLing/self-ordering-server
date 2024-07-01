package hust.server.app.endpoint.rest;

import hust.server.app.service.ResponseFactory;
import hust.server.domain.products.service.MenuService;
import hust.server.domain.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/guest/v1/menu")
    public ResponseEntity<?> getMenuForGuest(@RequestParam Long branchId){
        return ResponseFactory.response(menuService.getMenuForGuest(branchId));
    }

    @GetMapping("/cashier/v1/menu")
    public ResponseEntity<?> cashierGetMenu(@RequestParam String userId){
        return ResponseFactory.response(menuService.cashierGetMenu(userId));
    }
}
