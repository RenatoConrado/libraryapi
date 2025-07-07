package io.github.renatoconrado.libraryapi.security;

import io.github.renatoconrado.libraryapi.configuration.SecurityConfiguration;
import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component public class LoginSocialSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    @Override public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws ServletException, IOException {
        if (authentication instanceof OAuth2AuthenticationToken authToken) {
            authentication = processoAuth2(authToken);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private CustomAuthentication processoAuth2(OAuth2AuthenticationToken authToken)
        throws IllegalArgumentException {
        String email = authToken.getPrincipal().getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("email is null");
        }
        String login = email.substring(0, email.indexOf("@"));
        User user = userService.getByEmail(email).orElseGet(() -> {
            User newUser = new User(
                login,
                email,
                "123",
                List.of(SecurityConfiguration.ROLE_USER)
            );
            userService.save(newUser);
            return newUser;
        });
        return new CustomAuthentication(user);
    }
}
