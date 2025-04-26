package SJ_Project.DrawD.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {
    private final jwtUtil jUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if(httpRequest.getRequestURI().equals("/search/material")) {
            chain.doFilter(request, response);
        }
        else {
            try {
                String token = resolveToken((HttpServletRequest) request);
                String profileStatus = httpRequest.getParameter("profile");

                if(token != null && jUtil.validToken(token)) {
                    Authentication authentication = jUtil.getAuthentication(token, profileStatus);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else {
                    throw new JwtException("Authentication failed. Please log in again.");
                }

                chain.doFilter(request, response);
            } catch (JwtException | IllegalArgumentException e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json;charset=UTF-8");

                Map<String, String> errors = new HashMap<>();
                errors.put("error", "Authentication failed. Please log in again.");
                BaseResponse<?> errorResponse = BaseResponse.error(HttpStatus.UNAUTHORIZED, errors);
                httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        else {
            Cookie[] cookies = request.getCookies();

            if(cookies != null) {
                for(Cookie cookie : cookies) {
                    if("DrawDRefreshToken".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        }

        throw new JwtException("No Jwt");
    }
}
