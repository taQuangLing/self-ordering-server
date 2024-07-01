package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GuestOrderItemResponse {
    private String productName;
    private String productPrice;
    private Long id;
    private String productImg;
    private Integer quantity;
    private Integer hasSize;
    private String sizeSelected;
    private String note;
}
