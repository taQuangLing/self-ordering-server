package hust.server.domain.authen.entities;

import hust.server.domain.authen.dto.response.PositionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "positions")
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "created_by")
    private String createdBy;

    public PositionResponse toPositionResponse() {
        return PositionResponse.builder()
                .id(id)
                .name(name)
                .build();
    }
}
