package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableResponse {
    private Long id;
    private String name;
    private String qrcode;
}
