package api.builders;

import java.util.HashMap;
import java.util.Map;

// Builder: valid item by default, each test changes only the field it cares about
public class ItemBuilder {

    private final Map<String, Object> item = new HashMap<>();

    public ItemBuilder() {
        item.put("name", "Test Item");
        item.put("description", "Created by automation");
        item.put("price", 25.5);
        item.put("stock", 10);
    }

    public ItemBuilder name(Object name) {
        item.put("name", name);
        return this;
    }

    public ItemBuilder description(Object description) {
        item.put("description", description);
        return this;
    }

    public ItemBuilder price(Object price) {
        item.put("price", price);
        return this;
    }

    public ItemBuilder stock(Object stock) {
        item.put("stock", stock);
        return this;
    }

    public ItemBuilder without(String field) {
        item.remove(field);
        return this;
    }

    public Map<String, Object> build() {
        return item;
    }
}
