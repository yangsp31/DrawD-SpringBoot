package SJ_Project.DrawD.DTO.authDTO;

import lombok.Getter;

@Getter
public class jwtDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public jwtDTO (String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
