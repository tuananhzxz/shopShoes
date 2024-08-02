package com.example.ShopShoes.config;

import com.example.ShopShoes.security.CustomUserDetailsService;
import com.example.ShopShoes.security.JwtAuthenticationEntryPoint;
import com.example.ShopShoes.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    private GoogleAuth2SuccessHandler googleAuth2SuccessHandler;


    @Bean
    public static PasswordEncoder passwordEncode() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((authorize) -> {
            authorize.requestMatchers("/*").permitAll();
            authorize.requestMatchers("/cart/**").authenticated();
            authorize.requestMatchers("/checkout/**").authenticated();
            authorize.requestMatchers("/admin/**").hasRole("ADMIN");
            authorize.requestMatchers("/admin/blog/**").hasRole("ADMIN");
            authorize.anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults())
                .formLogin(login -> login.loginPage("/logon").permitAll().loginProcessingUrl("/logon").defaultSuccessUrl("/admin", true))
                .formLogin(login -> login.loginPage("/login").permitAll().loginProcessingUrl("/login").defaultSuccessUrl("/", true).failureUrl("/login?error=true"))
                .logout(logout -> logout
                .logoutUrl("/log")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID"))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(googleAuth2SuccessHandler));
        http.exceptionHandling( exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedPage("/error-404")
        );
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/upload/**","/assets/**","/fe/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/tinymce/**");
    }
}
