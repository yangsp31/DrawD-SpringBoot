package SJ_Project.DrawD.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class userEntity {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "profile")
    private String profile;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder(toBuilder = true)
    public userEntity(String email, String name, String profile, String refreshToken) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.refreshToken = refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
