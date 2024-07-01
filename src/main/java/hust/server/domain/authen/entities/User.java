package hust.server.domain.authen.entities;

import hust.server.domain.BaseEntity;
import hust.server.domain.authen.dto.response.EmployeeDetailsResponse;
import hust.server.domain.authen.dto.response.EmployeeResponse;
import hust.server.domain.authen.dto.response.UserResponse;
import hust.server.domain.products.entity.Branch;
import hust.server.infrastructure.utilies.Utility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    String id;

    @Column
    String username;

    @Column
    String password;

    @Column
    String firstName;

    @Column
    String lastName;

    @Column
    String name;

    @Column
    String email;

    @Column
    String phone;

    @Column
    String role;

    @Column
    Integer active;

    @Column(name = "is_guest")
    Integer isGuest;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;

    @Column(name = "position_id")
    private Long positionId;

    @Column
    private String code;

    @Column(name = "created_by")
    private String createdBy;

    public CustomUserDetails toCustomUserDetails(){
        CustomUserDetails customUserDetails =  CustomUserDetails.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .role(this.role)
                .build();
        return customUserDetails;
    }

    public UserResponse toUserReponse(){
        return UserResponse.builder()
                .id(this.id)
                .username(this.username)
                .active(this.active)
                .role(this.role)
                .build();
    }

    public EmployeeResponse toEmployeeResponse() {
        return EmployeeResponse.builder()
                .id(id)
                .name(name)
                .code(code)
                .phoneNumber(phone)
                .status(convertStatus())
                .branch(branch.getAddress())
                .branchId(branch.getId())
                .build();
    }

    private String convertStatus(){
        switch (active){
            case 0:
                return "Không hoạt động";
            case 1:
                return "Hoạt động";
            default:
                return "";
        }

    }

    public EmployeeDetailsResponse toEmployeeDetailsResponse() {
        return EmployeeDetailsResponse.builder()
                .id(id)
                .branchId(branch.getId())
                .createdAt(Utility.toLocalDateTime(createdAt, ""))
                .email(email)
                .phone(phone)
                .positionId(positionId)
                .code(code)
                .name(name)
                .statusStr(convertStatus())
                .status(active == 1)
                .build();
    }
}
