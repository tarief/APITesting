package api.auth;

import java.util.Map;

// Strategy: each way of logging in gives its own headers, the clients don't care which one is used
public interface AuthStrategy {

    Map<String, String> headers();
}
