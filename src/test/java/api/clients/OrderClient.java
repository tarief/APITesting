package api.clients;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.util.Map;

public class OrderClient {

    private final APIRequestContext request;

    public OrderClient(APIRequestContext request) {
        this.request = request;
    }

    public APIResponse create(Map<String, Object> order) {
        return request.post("/orders", RequestOptions.create().setData(order));
    }

    public APIResponse checkout(String orderId) {
        return request.post("/orders/" + orderId + "/checkout");
    }

    public APIResponse getAll() {
        return request.get("/orders");
    }

    public APIResponse getPaid() {
        return request.get("/orders/paid");
    }
}
