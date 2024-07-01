package hust.server.domain.orders.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GuestOrderResponse {
    private Long id;
    private String orderAt;
    private List<GuestOrderItemResponse> orderItemList;
    private String status; // 0: Chờ xác nhận, 1: Đang pha chế, 2: Đã xong, 3: Đã hủy
}
