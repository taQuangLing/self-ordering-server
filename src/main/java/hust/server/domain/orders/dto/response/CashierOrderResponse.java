package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CashierOrderResponse {
    private Long id;
    private String code;
    private String total;
    private String orderAt;
    private String payments;
    private String isOrderAtTable;
    private List<CashierOrderItemResponse> orderItemList;
    private String status; // 0: Chờ xác nhận, 1: Đang pha chế, 2: Đã xong, 3: Đã hủy
    private Boolean isExpanded;
}
