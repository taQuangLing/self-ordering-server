package hust.server.domain.authen.dto.response;

import hust.server.domain.authen.entities.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String role;
    Integer active;

    public User toEntity() {
        return
            User.builder()
                .id(this.id)
                .username(this.username)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .role(this.role)
                .build();
    }
}


