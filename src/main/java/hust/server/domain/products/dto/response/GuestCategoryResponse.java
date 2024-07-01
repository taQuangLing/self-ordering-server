package hust.server.domain.products.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestCategoryResponse {
    private Long id;
    private String name;
    private String img;
    private Integer count;
    List<GuestProductResponse> products = new ArrayList<>();
}
