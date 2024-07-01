package hust.server.domain.authen.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponse {
    private String id;
    private String code;
    private String name;
    private String branch;
    private Long branchId;
    private String phoneNumber;
    private String position;
    private String status;
}
