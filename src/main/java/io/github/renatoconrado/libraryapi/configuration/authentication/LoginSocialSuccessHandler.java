package io.github.renatoconrado.libraryapi.configuration.authentication;

import io.github.renatoconrado.libraryapi.users.model.User;
import io.github.renatoconrado.libraryapi.users.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static io.github.renatoconrado.libraryapi.configuration.Constants.Roles;

@RequiredArgsConstructor
@Component
public class LoginSocialSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();

        if (authentication instanceof OAuth2AuthenticationToken authToken) {
            CustomAuthentication customAuthentication = processoOAuth2(authToken);
            context.setAuthentication(customAuthentication);
            super.onAuthenticationSuccess(request, response, customAuthentication);
        } else {
            context.setAuthentication(authentication);
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    /**
     * @param authToken OAuth2Token
     * @return auth if the user is registered, or a default one if is the first time
     * @throws IllegalArgumentException if the email is null
     */
    private CustomAuthentication processoOAuth2(OAuth2AuthenticationToken authToken) {
        String email = authToken.getPrincipal().getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("email is null");
        }

        String login = email.substring(0, email.indexOf('@'));

        User user = userService
            .getByEmail(email)
            .orElseGet(() -> {
                User defaultUser = new User(login, email, "123", List.of(Roles.USER));
                userService.save(defaultUser);
                return defaultUser;
            });

        return new CustomAuthentication(user);
    }
}
