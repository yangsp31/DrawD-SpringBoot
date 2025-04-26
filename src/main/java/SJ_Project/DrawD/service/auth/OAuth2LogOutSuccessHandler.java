package SJ_Project.DrawD.service.auth;

import SJ_Project.DrawD.DTO.authDTO.identificationDTO;
import SJ_Project.DrawD.service.repository.userRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LogOutSuccessHandler implements LogoutSuccessHandler {
    private final userRepository userRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Cookie cookie = new Cookie("DrawDRefreshToken", "");
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setDomain("drawd.store");
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();

            userRepository.deleteRefreshToken(idDTO.getEmail());
        }
    }
}
