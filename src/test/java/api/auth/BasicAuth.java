package api.auth;

import java.util.Base64;
import java.util.Map;

public class BasicAuth implements AuthStrategy {

    private final String username;
    private final String password;

    public BasicAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Map<String, String> headers() {
        String encoded = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        return Map.of("Authorization", "Basic " + encoded);
    }
}
