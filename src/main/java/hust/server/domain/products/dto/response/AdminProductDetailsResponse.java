package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminProductDetailsResponse {
    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private Integer status;
    private String statusDis;
    private String createdAt;
    private Integer hasSize;
    private Long price;
    private String img;
    private List<SizeResponse> sizeResponseList;
}
