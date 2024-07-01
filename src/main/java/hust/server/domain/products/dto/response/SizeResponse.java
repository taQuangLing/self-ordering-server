package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SizeResponse {
    private Long id;
    private String size;
    private String priceDisplay;
    private Integer isDefault;
    private Long price;
}
