package hust.server.domain.orders.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderUpdateRequest {
    private String promotions;
    private Integer payments;
    private Integer isOrderAtTable;
    private List<Long> itemCartList;
    private String userId;
    private Long id;
}
