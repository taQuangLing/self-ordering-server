package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerCartResponse {
    private String name;
    private String priceDisplay;
    private Long price;
    private Long id;
    private String img;
    private Integer isCheck;
    private Integer quantity;
    private Integer hasSize;
    private String sizeSelected;
    private String note;
}
