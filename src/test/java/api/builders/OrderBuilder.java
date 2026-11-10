package api.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderBuilder {

    private String customerId;
    private final List<Map<String, Object>> items = new ArrayList<>();

    public OrderBuilder customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderBuilder item(String itemId, int quantity) {
        items.add(Map.of("itemId", itemId, "quantity", quantity));
        return this;
    }

    public Map<String, Object> build() {
        return Map.of("customerId", customerId, "items", items);
    }
}
