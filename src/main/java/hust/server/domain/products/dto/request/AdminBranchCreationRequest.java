package hust.server.domain.products.dto.request;

import hust.server.domain.products.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminBranchCreationRequest {
    private String name;
    private String address;
    private String town;
    private String city;
    private String createdBy;
    private String logo;

    public Branch toBranchEntity() {
        return Branch.builder()
                .name(name)
                .address(address)
                .town(town)
                .city(city)
                .createdBy(createdBy)
                .logo(logo)
                .active(1)
                .code(UUID.randomUUID().toString())
                .build();
    }
}
