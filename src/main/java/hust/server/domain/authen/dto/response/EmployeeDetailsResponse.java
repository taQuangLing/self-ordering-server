package hust.server.domain.authen.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDetailsResponse {
    private String id;
    private String code;
    private String name;
    private Long branchId;
    private String phone;
    private String email;
    private Long positionId;
    private String createdAt;
    private String statusStr;
    private Boolean status;
}
