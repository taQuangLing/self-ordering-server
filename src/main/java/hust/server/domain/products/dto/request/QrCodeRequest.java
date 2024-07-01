package hust.server.domain.products.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QrCodeRequest {
    private String url;
    private String path;
    private Integer width;
    private Integer height;
    private Long branchId;
}
