package hust.server.app.endpoint.rest;

import hust.server.app.service.ResponseFactory;
import hust.server.domain.products.dto.request.AdminBranchCreationRequest;
import hust.server.domain.products.dto.request.AdminBranchUpdateRequest;
import hust.server.domain.products.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class BranchController {
    @Autowired
    private BranchService branchService;

    @GetMapping("/admin/v1/branches/filter")
    public ResponseEntity<?> getBranchesFilter(@RequestParam String userId){
        return ResponseFactory.response(branchService.getBranchesFilter(userId));
    }

    @GetMapping("/admin/v1/branches")
    public ResponseEntity<?> getBranches(@RequestParam String userId){
        return ResponseFactory.response(branchService.getBranches(userId));
    }

    @GetMapping("/admin/v1/branches/{id}")
    public ResponseEntity<?> getBranchDetails(@RequestParam String userId,
                                              @PathVariable Long id){
        return ResponseFactory.response(branchService.getBranchesDetails(id, userId));
    }

    @PostMapping("/admin/v1/branches")
    public ResponseEntity<?> createBranch(@RequestBody AdminBranchCreationRequest request) throws IOException {
        return ResponseFactory.response(branchService.createBranch(request));
    }

    @DeleteMapping("/admin/v1/branches/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id,
                                          @RequestParam String userId){
        return ResponseFactory.response(branchService.deleteBranch(id, userId));
    }

    @PutMapping("/admin/v1/branches/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id,
                                          @RequestBody AdminBranchUpdateRequest request){
        request.setId(id);
        return ResponseFactory.response(branchService.updateBranch(request));
    }
}
