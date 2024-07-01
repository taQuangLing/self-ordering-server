package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminMenuResponse {
    private Long id;
    private List<Object> menuItem;
}
