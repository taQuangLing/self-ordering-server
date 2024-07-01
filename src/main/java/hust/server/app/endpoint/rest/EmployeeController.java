package hust.server.app.endpoint.rest;

import hust.server.app.service.ResponseFactory;
import hust.server.domain.authen.dto.request.EmployeeUpdateRequest;
import hust.server.domain.authen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/v1/employees")
    public ResponseEntity<?> getEmployees(@RequestParam String userId){
        return ResponseFactory.response(userService.getEmployees(userId));
    }

    @GetMapping("/admin/v1/employees/{id}")
    public ResponseEntity<?> getEmployeeDetails(@PathVariable String id, @RequestParam String userId){
        return ResponseFactory.response(userService.getEmployeeDetails(id, userId));
    }

    @PutMapping("/admin/v1/employees/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable String id,
            @RequestParam String userId,
            @RequestBody EmployeeUpdateRequest request)
    {
        request.setId(id);
        return ResponseFactory.response(userService.updateEmployee(userId, request));
    }

    @DeleteMapping("/admin/v1/employees/{id}")
    public ResponseEntity<?> deleteEmployee(
            @PathVariable String id,
            @RequestParam String userId){
        return ResponseFactory.response(userService.deleteEmployee(id, userId));
    }

    @GetMapping("/admin/v1/employees/manager")
    public ResponseEntity<?> getEmployeeManager(@RequestParam String userId){
        return ResponseFactory.response(userService.getEmployeeManager(userId));
    }
}
