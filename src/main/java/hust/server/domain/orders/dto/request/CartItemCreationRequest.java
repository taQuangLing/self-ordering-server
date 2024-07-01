package hust.server.domain.orders.dto.request;

import hust.server.domain.orders.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemCreationRequest {
    private String userId;
    private Long productId;
    private Long sizeSelectedId;
    private Integer quantity;
    private String note;

    public CartItem toCustomerCartItemEntity() {
        return CartItem.builder()
                .sizeSelectedId(sizeSelectedId)
                .quantity(quantity)
                .note(note)
                .build();
    }
}
