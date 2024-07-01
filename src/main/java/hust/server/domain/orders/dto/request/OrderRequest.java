package hust.server.domain.orders.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderRequest {
    private String total;
    private Integer payments;
    private Integer isOrderAtTable;
    private String userId;
    private List<OrderItemRequest> orderItemList;
}
