package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CashierOrderItemResponse {
    private String productName;
    private String productPrice;
    private Long id;
    private Integer quantity;
    private String sizeSelected;
    private String note;
}
