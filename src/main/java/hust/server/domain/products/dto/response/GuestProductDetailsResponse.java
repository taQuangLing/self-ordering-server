package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GuestProductDetailsResponse {
    private Long id;
    private String name;
    private String summary;
    private String img;
    private List<SizeResponse> sizes;
    private Integer hasSize;
    private String priceDisplay;
    private Long price;
    private String description;
}
