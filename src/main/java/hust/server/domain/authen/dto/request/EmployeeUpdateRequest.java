package hust.server.domain.authen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeUpdateRequest {
    private String id;
    private String name;
    private Long branchId;
    private String phone;
    private String email;
    private Long positionId;
    private Integer status;
}
