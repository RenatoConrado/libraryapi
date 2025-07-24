package io.github.renatoconrado.libraryapi.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.renatoconrado.libraryapi.configuration.authentication.CustomAuthentication;
import io.github.renatoconrado.libraryapi.configuration.authentication.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static io.github.renatoconrado.libraryapi.configuration.Constants.Roles;
import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.ACCESS_TOKEN;

@EnableWebSecurity
@Configuration
public class AuthorizationServerConfiguration {

    private static final String KEY_PAIR_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    /**
     * @see CustomAuthenticationProvider
     * @deprecated
     */
    @Deprecated
    public static UserDetailsService oldUserDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User
            .builder()
            .username("user")
            .password(passwordEncoder.encode("123"))
            .roles(Roles.USER)
            .build();

        UserDetails admin = User
            .builder()
            .username("admin")
            .password(passwordEncoder.encode("123"))
            .roles(Roles.ADMIN)
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Deprecated
    public static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("GRUPO_");
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
        HttpSecurity httpSecurity
    ) throws Exception {

        var serverConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();

        httpSecurity
            .securityMatcher(serverConfigurer.getEndpointsMatcher())
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .with(
                serverConfigurer,
                authorizationServer -> authorizationServer.oidc(Customizer.withDefaults())
            )
            .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()))
            .exceptionHandling(exceptions ->
                exceptions.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/login"),
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            );

        return httpSecurity.build();
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
//            Access Token: Token das transações
            .accessTokenTimeToLive(Duration.ofMinutes(60))
//            Refresh Toke: Renova o Access Token
            .refreshTokenTimeToLive(Duration.ofMinutes(90)).build();
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings.builder().requireAuthorizationConsent(false).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean //JWK - JSON Web Key
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
        RSAKey rsaKey = generateRSAKey();
        var jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private RSAKey generateRSAKey() throws NoSuchAlgorithmException {
        var keyPairGenerator = KeyPairGenerator.getInstance(KEY_PAIR_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
            .Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings
            .builder()
            .authorizationEndpoint("/oauth2/authorize")
            .pushedAuthorizationRequestEndpoint("/oauth2/par")
            .deviceAuthorizationEndpoint("/oauth2/device_authorization")
            .deviceVerificationEndpoint("/oauth2/device_verification")
//            Obter token
            .tokenEndpoint("/oauth2/token")
//            Informações do token
            .tokenIntrospectionEndpoint("/oauth2/introspect")
//            Revogar token antes de acabar o tempo
            .tokenRevocationEndpoint("/oauth2/revoke")
//            Obter a chave publica para verificar a assinatura do token
            .jwkSetEndpoint("/oauth2/jwks")
            .oidcLogoutEndpoint("/connect/logout")
//            Informações do usuário OPEN ID CONNECT
            .oidcUserInfoEndpoint("/userinfo")
            .oidcClientRegistrationEndpoint("/connect/register")
            .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return jwtEncodingContext -> {

            Authentication principal = jwtEncodingContext.getPrincipal();
            if (!(principal instanceof CustomAuthentication authentication)) {
                return;
            }

            OAuth2TokenType tokenType = jwtEncodingContext.getTokenType();
            if (!tokenType.equals(ACCESS_TOKEN)) {
                return;
            }

            List<String> scope = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

            jwtEncodingContext
                .getClaims()
                .claim("authorities", scope)
                .claim("email", authentication.getUser().getEmail());
        };
    }
}
