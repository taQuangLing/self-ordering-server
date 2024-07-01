package hust.server.app.endpoint.rest;

import hust.server.app.service.ResponseFactory;
import hust.server.domain.products.dto.request.AdminCategoryRequest;
import hust.server.domain.products.dto.request.AdminProductRequest;
import hust.server.domain.products.dto.request.AdminProductUpdatedRequest;
import hust.server.domain.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/guest/v1/products/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long id){
        return ResponseFactory.response(productService.getProductDetails(id));
    }

    @GetMapping("/admin/v1/products")
    public ResponseEntity<?> getProductByAdmin(@RequestParam String userId){
        return ResponseFactory.response(productService.getProductsByAdmin(userId));
    }

    @GetMapping("/admin/v1/categories")
    public ResponseEntity<?> getCategories(@RequestParam String userId){
        return ResponseFactory.response(productService.getCategories(userId));
    }

    @PostMapping("/admin/v1/products")
    public ResponseEntity<?> addProduct(@RequestBody AdminProductRequest request){
        return ResponseFactory.response(productService.addProduct(request));

    }

    @PostMapping("/admin/v1/categories")
    public ResponseEntity<?> createCategory(@RequestBody AdminCategoryRequest request){
        return ResponseFactory.response(productService.createCategory(request));
    }

    @DeleteMapping("/admin/v1/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestParam String userId){
        return ResponseFactory.response(productService.deleteProduct(id, userId));
    }

    @GetMapping("/admin/v1/products/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long id,
                                              @RequestParam String userId){
        return ResponseFactory.response(productService.adminGetProductDetails(id, userId));
    }

    @PutMapping("/admin/v1/products/{id}")
    public ResponseEntity<?> updateProduct(@RequestParam String userId,
                                           @PathVariable Long id,
                                           @RequestBody AdminProductUpdatedRequest request)
    {
        request.setId(id);
        request.setUserId(userId);
        return ResponseFactory.response(productService.updateProduct(request));

    }
}
