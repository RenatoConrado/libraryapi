package io.github.renatoconrado.libraryapi.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigurationConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class Roles {
        public static final String
            USER = "USER",
            ADMIN = "ADMIN";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class Endpoints {
        public static final String LOGIN = "/login";
        public static final String[]
            PERMISSIVE = { "/login/**", "/users" },
            SENSITIVE = { "/", "/authors/**", "/books/**" };
    }
}
