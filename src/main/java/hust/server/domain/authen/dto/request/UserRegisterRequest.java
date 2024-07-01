package hust.server.domain.authen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String name;
    private String createdBy;
    private Long branchId;
    private String phone;
    private String password;
    private String email;
    private Long positionId;
    private String role;
}
