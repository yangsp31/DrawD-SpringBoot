package SJ_Project.DrawD.service.auth;

import SJ_Project.DrawD.DTO.authDTO.jwtDTO;
import SJ_Project.DrawD.Entity.userEntity;
import SJ_Project.DrawD.service.repository.userRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final jwtUtil jUtil;
    private final userRepository userRepository;

    @Override
    public  void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        jwtDTO jwt = jUtil.getToken(email, name);

        if(!userRepository.existUser(email)) {
            userEntity user = new userEntity(email, name, picture, jwt.getRefreshToken());
            userRepository.saveUser(user);
        }
        else {
            userRepository.updateRefreshToken(email, jwt.getRefreshToken());
            userRepository.updateProfile(email, picture);
        }

        Cookie accessTokenCookie = new Cookie("DrawDAccessToken", jwt.getAccessToken());
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setDomain("drawd.store");
        accessTokenCookie.setMaxAge(20);

        Cookie refreshTokenCookie = new Cookie("DrawDRefreshToken", jwt.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setDomain("drawd.store");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);

        response.sendRedirect("https://do.drawd.store/loading");
    }
}
