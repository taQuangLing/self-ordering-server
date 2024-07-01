package hust.server.domain.products.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminBranchDetailsResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String town;
    private String createdAt;
    private String statusDis;
    private Integer status;
    private String logo;
    private String code;
    private String qrcode;
    private List<AdminMenuItemResponse> menuItemRes;
}
