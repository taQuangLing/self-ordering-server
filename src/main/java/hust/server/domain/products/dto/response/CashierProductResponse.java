package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CashierProductResponse {
    private Long id;
    private String name;
    private String price;
    private String img;
    private Integer hasSize;
    private List<SizeResponse> sizes;
}
