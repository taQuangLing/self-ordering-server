package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CashierCategoryResponse {
    private Long id;
    private String name;
    private String img;
    private Integer count;
    List<CashierProductResponse> products;
}
