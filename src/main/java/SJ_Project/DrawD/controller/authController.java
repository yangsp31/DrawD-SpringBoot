package SJ_Project.DrawD.controller;

import SJ_Project.DrawD.DTO.authDTO.identificationDTO;
import SJ_Project.DrawD.DTO.authDTO.jwtDTO;
import SJ_Project.DrawD.service.auth.jwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class authController {

    private final jwtUtil jUtil;

    public authController(jwtUtil jUtil) {
        this.jUtil = jUtil;
    }

    @GetMapping("/check/login")
    public ResponseEntity<String> checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();

            if(idDTO.getProfile().equals("NR")) {
                return new ResponseEntity<>("Success", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(idDTO.getProfile(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Authentication Error", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/refresh/jwt")
    public ResponseEntity<String> refresh(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();
            jwtDTO jwt = jUtil.refreshToken(idDTO);

            if(jwt.getRefreshToken() != null) {
                Cookie refreshTokenCookie = new Cookie("DrawDRefreshToken", jwt.getRefreshToken());
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setDomain("drawd.store");
                refreshTokenCookie.setSecure(true);
                refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

                response.addCookie(refreshTokenCookie);
            }

            Cookie accessTokenCookie = new Cookie("DrawDAccessToken", jwt.getAccessToken());
            accessTokenCookie.setHttpOnly(false);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setDomain("drawd.store");
            accessTokenCookie.setMaxAge(20);

            response.addCookie(accessTokenCookie);

            if(idDTO.getProfile().equals("NR")) {
                return new ResponseEntity<>("Success", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(idDTO.getProfile(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Authentication Error", HttpStatus.UNAUTHORIZED);
    }

}
