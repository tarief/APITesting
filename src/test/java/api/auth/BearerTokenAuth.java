package api.auth;

import java.util.Map;

public class BearerTokenAuth implements AuthStrategy {

    private final String token;

    public BearerTokenAuth(String token) {
        this.token = token;
    }

    @Override
    public Map<String, String> headers() {
        return Map.of("Authorization", "Bearer " + token);
    }
}
