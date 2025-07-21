package io.github.renatoconrado.libraryapi.configuration.authentication;

import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            User user = userService
                .getByUsername(jwtAuth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

            Authentication customAuthentication = new CustomAuthentication(user);
            context.setAuthentication(customAuthentication);
            System.out.println(customAuthentication.getPrincipal());
        } else if (authentication != null) {
            System.out.println("Não é JwtAuthenticationToken: " + authentication.getPrincipal());
        }

        filterChain.doFilter(request, response);
    }

}
