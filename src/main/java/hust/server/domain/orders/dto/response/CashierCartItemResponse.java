package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CashierCartItemResponse {
    private String name;
    private String priceDisplay;
    private Long price;
    private Long id;
    private Integer quantity;
    private Integer hasSize;
    private String sizeSelected;
//    private String note;
}
