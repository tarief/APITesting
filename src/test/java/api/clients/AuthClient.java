package api.clients;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.util.HashMap;
import java.util.Map;

public class AuthClient {

    private final APIRequestContext request;

    public AuthClient(APIRequestContext request) {
        this.request = request;
    }

    // a null value means the field is left out of the body
    public APIResponse login(String username, String password) {
        Map<String, Object> body = new HashMap<>();
        if (username != null) body.put("username", username);
        if (password != null) body.put("password", password);
        return request.post("/login", RequestOptions.create().setData(body));
    }
}
