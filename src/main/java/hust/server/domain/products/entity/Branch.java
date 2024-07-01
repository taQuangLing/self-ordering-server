package hust.server.domain.products.entity;

import hust.server.domain.BaseEntity;
import hust.server.domain.products.dto.response.AdminBranchDetailsResponse;
import hust.server.domain.products.dto.response.AdminBranchFilterResponse;
import hust.server.domain.products.dto.response.AdminBranchResponse;
import hust.server.infrastructure.utilies.Utility;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@Entity
@Builder
@Table(name = "branches")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Branch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String town;

    @Column(length = 50)
    private String city;


    @Column(length = 100)
    private String address;

    @Column(name = "created_by")
    private String createdBy;

    @Column
    private Integer active;

    @Column
    private String logo;

    @Column
    private String qrcode;

    @Column
    private String code;

    public AdminBranchResponse toAdminBranchResponse() {
        return AdminBranchResponse.builder()
                .id(id)
                .address(address)
                .name(name)
                .createdAt(Utility.toLocalDateTime(createdAt, ""))
                .status(convertStatus())
                .build();
    }
    public AdminBranchFilterResponse toAdminBranchFilterResponse() {
        return AdminBranchFilterResponse.builder()
                .id(id)
                .address(address)
                .build();
    }

    private String convertStatus(){
        switch (active) {
            case 0:
                return "Không hoạt động";
            case 1:
                return "Hoạt động";
            default:
                return "";
        }
    }

    public AdminBranchDetailsResponse toAdminBranchDetailResponse() {
        return AdminBranchDetailsResponse.builder()
                .id(id)
                .name(name)
                .address(address)
                .city(city)
                .town(town)
                .status(active)
                .statusDis(convertStatus())
                .logo(logo)
                .code(code)
                .qrcode(qrcode)
                .createdAt(Utility.toLocalDateTime(createdAt, ""))
                .menuItemRes(new ArrayList<>())
                .build();
    }
}
