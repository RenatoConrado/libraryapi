package io.github.renatoconrado.libraryapi.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Roles {
        public static final String USER = "USER", ADMIN = "ADMIN";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Endpoints {
        public static final String LOGIN = "/login";
        public static final String[]
            PERMISSIVE = { "/login/**", "/users" },
            SENSITIVE = { "/", "/authors/**", "/books/**" },
            SWAGGER_DOCS = {
                "/v2/api-docs/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjar/**"
            },
            ACTUATOR = { "/actuator/**" },
            IGNORE = combine(SWAGGER_DOCS, ACTUATOR);

        private static String[] combine(String[]... arrays) {
            return Arrays.stream(arrays).flatMap(Arrays::stream).toArray(String[]::new);
        }
    }
}
