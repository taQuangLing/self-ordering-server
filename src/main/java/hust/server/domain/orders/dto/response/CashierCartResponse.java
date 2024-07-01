package hust.server.domain.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashierCartResponse {
    private Long id;
    private List<CashierCartItemResponse> cartItemList;
    private Integer isOrderAtTable;
    private Integer payments;
    private Long orderId;
}
