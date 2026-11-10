package api.auth;

import java.util.Map;

public class NoAuth implements AuthStrategy {

    @Override
    public Map<String, String> headers() {
        return Map.of();
    }
}
