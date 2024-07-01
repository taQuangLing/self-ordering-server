package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminMenuItemResponse {
    private Long id;
    private String productName;
    private String img;
    private Boolean active;
}
