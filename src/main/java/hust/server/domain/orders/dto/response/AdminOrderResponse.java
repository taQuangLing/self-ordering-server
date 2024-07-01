package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminOrderResponse {
    private Long id;
    private String datetime;
    private String branch;
    private Long branchId;
    private String payments;
    private String status;
    private String total;
    private String note;
}
