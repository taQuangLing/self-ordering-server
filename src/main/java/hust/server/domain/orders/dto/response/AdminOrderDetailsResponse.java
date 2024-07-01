package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminOrderDetailsResponse {
    private Long id;
    private String branch;
    private String payments;
    private String status;
    private String datetime;
    private List<AdminOrderItemResponse> orderItem;
    private String note;
}
