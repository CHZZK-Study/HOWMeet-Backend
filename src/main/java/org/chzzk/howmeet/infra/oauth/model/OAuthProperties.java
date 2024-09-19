package org.chzzk.howmeet.infra.oauth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "oauth")
@Getter
@ToString
public class OAuthProperties {
    private final Map<String, Client> client = new HashMap<>();
    private final Map<String, Provider> provider = new HashMap<>();

    public Client getClientFrom(final String key) {
        return client.get(key);
    }

    public Provider getProviderFrom(final String key) {
        return provider.get(key);
    }

    @Getter
    @Setter
    @ToString
    public static class Client {
        private String id;
        private String secret;
        private String redirectUri;
        private Set<String> scope;
    }

    @Getter
    @Setter
    @ToString
    public static class Provider {
        private Authorize authorize;
        private String name;
        private Token token;
        private Profile profile;
    }

    @Getter
    @Setter
    @ToString
    public static class Authorize {
        private String response_type;
        private String method;
        private String url;
    }

    @Getter
    @Setter
    @ToString
    public static class Token {
        private Issue issue;
    }

    @Getter
    @Setter
    @ToString
    public static class Issue {
        private String grant_type;
        private String method;
        private String url;
    }

    @Getter
    @Setter
    @ToString
    public static class Profile {
        private String method;
        private String url;
    }
}
