package hust.server.domain.products.dto.request;

import hust.server.domain.products.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoryRequest {
    private String userId;
    private String name;
    private String description;
    private String img;

    public Category toCategoryEntity(){
        return Category.builder()
                .createdBy(userId)
                .name(name)
                .summary(description)
                .img(img)
                .active(1)
                .build();
    }
}
