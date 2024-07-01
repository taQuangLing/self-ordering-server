package hust.server.domain.orders.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckCartItemRequest {
    private Long cartItemId;
    private Integer isCheck;
}
