package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminOrderItemResponse {
    private Long id;
    private String name;
    private Integer quantity;
    private Long price;
    private String priceDisplay;
    private String size;
}
