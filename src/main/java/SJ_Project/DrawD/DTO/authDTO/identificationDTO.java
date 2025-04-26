package SJ_Project.DrawD.DTO.authDTO;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
public class identificationDTO {
    private String name;
    private String email;
    private String profile;
    private String type;
    private Collection<GrantedAuthority> authorities;

    public identificationDTO(String name, String email, String profile, String type) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.type = type;
        this.authorities = Collections.emptyList();
    }
}
