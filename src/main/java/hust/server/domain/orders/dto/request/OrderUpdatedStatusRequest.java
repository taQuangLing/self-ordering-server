package hust.server.domain.orders.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderUpdatedStatusRequest {
    private String userId;
    private Long id;
    private Integer status;
}
