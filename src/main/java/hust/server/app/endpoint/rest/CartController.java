package hust.server.app.endpoint.rest;

import hust.server.app.service.ResponseFactory;
import hust.server.domain.orders.dto.request.*;
import hust.server.domain.orders.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/guest/v1/carts")
    public ResponseEntity<?> getCartsList(@RequestParam(name = "userId") String userId){
        return ResponseFactory.response(cartService.guestGetCartList(userId));
    }

    @PostMapping("/guest/v1/carts")
    public ResponseEntity<?> addCartItem(@RequestBody CartItemCreationRequest request){
        return ResponseFactory.response(cartService.addCartItem(request));
    }

    @PostMapping("/cashier/v1/carts")
    public ResponseEntity<?> addToCartItem(@RequestBody CartItemCreationRequest request){
        return ResponseFactory.response(cartService.addCartItem(request));
    }

    @PutMapping("/guest/v1/carts/{id}/is_check")
    public ResponseEntity<?> checkCart(@PathVariable Long id,
                                       @RequestBody CheckCartItemRequest request)
    {
        request.setCartItemId(id);
        return ResponseFactory.response(cartService.checkCart(request));
    }

    @PutMapping("/guest/v1/carts/{id}/quantity")
    public ResponseEntity<?> updateQuantity(@PathVariable Long id,
                                            @RequestBody QuantityCartItemUpdateRequest request)
    {
        request.setCartItemId(id);
        return ResponseFactory.response(cartService.updateQuantity(request));
    }

    @PutMapping("/guest/v1/carts/{id}/note")
    public ResponseEntity<?> updateNote(@PathVariable Long id,
                                            @RequestBody CartUpdateNoteRequest request)
    {
        request.setId(id);
        return ResponseFactory.response(cartService.updateNote(request));
    }

    @DeleteMapping("/guest/v1/cartItem/{id}")
    public ResponseEntity<?> guestDeleteCart(@PathVariable Long id){
        return ResponseFactory.response(cartService.deleteCartItem(id));
    }

    @GetMapping("/cashier/v1/carts")
    public ResponseEntity<?> cashierGetCarts(@RequestParam(name = "userId") String userId){
        return ResponseFactory.response(cartService.cashierGetCartList(userId));
    }

    @PutMapping("/cashier/v1/carts/{id}/quantity")
    public ResponseEntity<?> cashierUpdateQuantity(@PathVariable Long id,
                                            @RequestBody QuantityCartItemUpdateRequest request)
    {
        request.setCartItemId(id);
        return ResponseFactory.response(cartService.updateQuantity(request));
    }

    @DeleteMapping("/cashier/v1/cartItem/{id}")
    public ResponseEntity<?> cashierDeleteCartItem(@PathVariable Long id){
        return ResponseFactory.response(cartService.deleteCartItem(id));
    }

    @PutMapping("/cashier/v1/carts/orders/{id}")
    public ResponseEntity<?> mapOrderToCart(@PathVariable Long id){
        return ResponseFactory.response(cartService.mapOrderToCart(id));
    }

    @DeleteMapping("/cashier/v1/carts/{userId}")
    public ResponseEntity<?> cashierDeleteCart(@PathVariable String userId){
        return ResponseFactory.response(cartService.deleteCart(userId));
    }
}
