package at.boot.responses;

import at.boot.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {

    private Long id;
    private String username;
    private Role role;

    private Date jwtexpirationDate;
    private Date jwtissuedAtDate;

}
