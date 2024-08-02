package com.example.ShopShoes.config;

import com.example.ShopShoes.entity.Role;
import com.example.ShopShoes.entity.User;
import com.example.ShopShoes.repository.RoleRepository;
import com.example.ShopShoes.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GoogleAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = Logger.getLogger(GoogleAuth2SuccessHandler.class.getName());

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            String email = token.getPrincipal().getAttributes().get("email").toString();
            logger.info("User email from token: " + email);

            Optional<User> userOptional = userRepository.findUserByEmail(email);
            User user;

            if (userOptional.isPresent()) {
                user = userOptional.get();
                logger.info("User found: " + user.getUsername());

                authenticateUser(user);
            } else {
                user = new User();
                user.setEmail(email);
                user.setUsername(token.getPrincipal().getAttributes().get("name").toString());
                logger.info("Creating new user: " + user.getUsername());

                Set<Role> roles = new HashSet<>();
                Role role = roleRepository.findByName("ROLE_USER");
                if (role == null) {
                    logger.log(Level.SEVERE, "Role 'ROLE_USER' not found");
                    throw new ServletException("Role 'ROLE_USER' not found");
                }
                roles.add(role);
                user.setRoles(roles);

                user = userRepository.save(user);
                if (user.getUserId() == null) {
                    logger.log(Level.SEVERE, "User not saved: " + user.getUsername());
                    throw new ServletException("User not saved: " + user.getUsername());
                }
                logger.info("User created and saved: " + user.getUsername());

                authenticateUser(user);
            }

            logger.info("Redirecting to home page.");
            redirectStrategy.sendRedirect(request, response, "/");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during OAuth2 authentication success handling", e);
            throw new ServletException("Error during OAuth2 authentication success handling: " + e.getMessage(), e);
        }
    }

    private void authenticateUser(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        Authentication authentications = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentications);
    }
}
