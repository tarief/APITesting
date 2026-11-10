package api.clients;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.util.Map;

public class ItemClient {

    private final APIRequestContext request;

    public ItemClient(APIRequestContext request) {
        this.request = request;
    }

    public APIResponse create(Map<String, Object> item) {
        return request.post("/items", RequestOptions.create().setData(item));
    }

    public APIResponse getAll() {
        return request.get("/items");
    }

    public APIResponse get(String id) {
        return request.get("/items/" + id);
    }
}
