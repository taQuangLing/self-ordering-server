package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminProductResponse {
    private Long id;
    private String img;
    private String name;
    private Long categoryId;
    private String categoryName;
    private String priceDisplay;
    private String status;
}
