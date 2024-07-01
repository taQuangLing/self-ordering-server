package hust.server.domain.authen.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionResponse {
    private Long id;
    private String name;
}
