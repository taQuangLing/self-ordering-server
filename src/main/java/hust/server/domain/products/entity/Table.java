package hust.server.domain.products.entity;

import hust.server.domain.products.dto.response.TableResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;


@Data
@Entity
@Builder
@javax.persistence.Table(name = "tables")
@AllArgsConstructor
@NoArgsConstructor
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String qrcode;

    @Column(name = "is_deleted", columnDefinition = "INT default 0")
    private Integer isDeleted;

    public TableResponse toTableResponse() {
        return TableResponse.builder()
                .id(id)
                .name(name)
                .qrcode(qrcode)
                .build();
    }
}
