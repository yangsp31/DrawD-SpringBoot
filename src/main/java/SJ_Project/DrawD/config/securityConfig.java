package SJ_Project.DrawD.config;

import SJ_Project.DrawD.service.auth.JwtAuthenticationFilter;
import SJ_Project.DrawD.service.auth.OAuth2LogOutSuccessHandler;
import SJ_Project.DrawD.service.auth.OAuth2SuccessHandler;
import SJ_Project.DrawD.service.auth.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class securityConfig {
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2LogOutSuccessHandler oAuth2LogOutSuccessHandler;
    private final jwtUtil jUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                //.cors().disable()
                .csrf(AbstractHttpConfigurer :: disable)
                .httpBasic(AbstractHttpConfigurer :: disable)
                .formLogin(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/search/material").permitAll()
                        .anyRequest().authenticated())
                /*.formLogin(formLogin -> formLogin
                        .loginPage("/").defaultSuccessUrl("/"))*/
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(oAuth2LogOutSuccessHandler)
                        .invalidateHttpSession(true))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2SuccessHandler))
                .addFilterBefore(new JwtAuthenticationFilter(jUtil), UsernamePasswordAuthenticationFilter.class);


        return security.build();
    }
}
