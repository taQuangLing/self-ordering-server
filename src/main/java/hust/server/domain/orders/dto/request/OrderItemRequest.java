package hust.server.domain.orders.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private Long sizeSelectedId;
}
