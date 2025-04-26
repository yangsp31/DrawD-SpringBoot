package SJ_Project.DrawD.service.auth;

import SJ_Project.DrawD.DTO.authDTO.identificationDTO;
import SJ_Project.DrawD.DTO.authDTO.jwtDTO;
import SJ_Project.DrawD.service.repository.userRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Component
public class jwtUtil {
    private final Key accessKey;
    private final Key refreshKey;
    private final userRepository userRepository;

    public jwtUtil(@Value("${jwt.access.secret}") String accessKey,
                   @Value("${jwt.refresh.secret}") String refreshKey,
                   userRepository userRepository) {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessKey);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshKey);

        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.userRepository = userRepository;
    }

    public jwtDTO getToken(String email, String name) {
        try {
            long now = (new Date()).getTime();
            Date accessExpires = new Date(now + (60 * 60 * 1000));
            Date refreshExpires = new Date(now + (7 * 24 * 60 * 60 * 1000));

            String accessToken = Jwts.builder()
                    .subject(name).claim("email", email).claim("type", "access")
                    .expiration(accessExpires)
                    .signWith(accessKey)
                    .compact();

            String refreshToken = Jwts.builder()
                    .subject(name).claim("email", email).claim("type", "refresh")
                    .expiration(refreshExpires)
                    .signWith(refreshKey)
                    .compact();

            return new jwtDTO("Bearer", accessToken, refreshToken);
        } catch (Exception e) {
            System.out.println(e);

            return null;
        }
    }

    public Authentication getAuthentication(String token, String profileStatus) {
        Claims claims = getClaims(token);

        String name = String.valueOf(Optional.ofNullable(claims.getSubject()).orElseThrow(() -> new RuntimeException("잘못된 토큰 입니다")));
        String email = Optional.ofNullable(claims.get("email", String.class)).orElseThrow(() -> new RuntimeException("잘못된 토큰입니다"));
        String type = Optional.ofNullable(claims.get("type", String.class)).orElseThrow(() -> new RuntimeException("잘못된 토큰 입니다"));

        if(type.equals("refresh")) {
            if(checkRefreshTokenExpire(token)) {
                type = "renew";
            }
        }

        if(profileStatus.equals("Empty")) {
            identificationDTO idDTO = new identificationDTO(name, email, userRepository.getProfile(email), type);

            return new UsernamePasswordAuthenticationToken(idDTO, "", Collections.emptyList());
        }
        else {
            identificationDTO idDTO = new identificationDTO(name, email, "NR", type);

            return new UsernamePasswordAuthenticationToken(idDTO, "", Collections.emptyList());
        }
    }

    public boolean validToken(String userToken) {
        try {
            getClaims(userToken);

            return true;
        }
        catch (ExpiredJwtException e) {
            System.out.println("만료된 토큰입니다.");
            throw new JwtException("만료된 토큰입니다.");
        }
        catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 형식의 토큰입니다.");
            throw new JwtException("지원하지 않는 형식의 토큰입니다.");
        }
        catch (MalformedJwtException e) {
            System.out.println("잘못된 형식의 토큰입니다.");
            throw new JwtException("잘못된 형식의 토큰입니다.");
        }
        catch (IllegalArgumentException e) {
            System.out.println("토큰이 null이거나 비어 있습니다.");
            throw new JwtException("토큰이 null이거나 비어 있습니다.");
        }
        catch (Exception e) {
            System.out.println("기타 오류 발생: " + e.getMessage());
            throw new JwtException("기타 오류 발생: " + e.getMessage());
        }
    }

    public Claims getClaims(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) accessKey).build().parseSignedClaims(token).getPayload();
            String type = claims.get("type", String.class);

            if(type.equals("access")) {
                return claims;
            }

            return null;
        } catch (JwtException e) {
            try {
                Claims claims = Jwts.parser().verifyWith((SecretKey) refreshKey).build().parseSignedClaims(token).getPayload();
                String type = claims.get("type", String.class);
                System.out.println(token);

                if(type.equals("refresh")){
                    if(userRepository.verifyRefreshToken(claims.get("email", String.class), token)) {
                        return claims;
                    }
                    else {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    throw new RuntimeException(e);
                }
            } catch (JwtException | IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public boolean checkRefreshTokenExpire(String token) {
        Date expiration = Jwts.parser().verifyWith((SecretKey) refreshKey).build().parseSignedClaims(token).getPayload().getExpiration();
        long time = expiration.getTime() - new Date().getTime();

        return time <= (24 * 60 * 60 * 1000);
    }

    public jwtDTO refreshToken(identificationDTO idDTO) {
        long now = (new Date()).getTime();
        Date accessExpires = new Date(now + (60 * 60 * 1000));

        String accessToken = Jwts.builder()
                .subject(idDTO.getName()).claim("email", idDTO.getEmail()).claim("type", "access")
                .expiration(accessExpires)
                .signWith(accessKey)
                .compact();

        if(idDTO.getType().equals("renew")) {
            Date refreshExpires = new Date(now + (7 * 24 * 60 * 60 * 1000));

            String refreshToken = Jwts.builder()
                    .subject(idDTO.getName()).claim("email", idDTO.getEmail()).claim("type", "refresh")
                    .expiration(refreshExpires)
                    .signWith(refreshKey)
                    .compact();

            userRepository.updateRefreshToken(idDTO.getEmail(), refreshToken);

            return new jwtDTO("Bearer", accessToken, refreshToken);
        }

        return new jwtDTO("Bearer", accessToken, null);
    }


}
