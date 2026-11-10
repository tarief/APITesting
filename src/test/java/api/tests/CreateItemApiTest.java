package api.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import api.auth.BasicAuth;
import api.auth.NoAuth;
import api.base.BaseApiTest;
import api.builders.ItemBuilder;
import api.clients.ItemClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.playwright.APIResponse;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateItemApiTest extends BaseApiTest {

    private ItemClient adminItems;

    @BeforeClass
    public void login() {
        adminItems = new ItemClient(loginAs(config.get("admin.username"), config.get("admin.password")));
    }

    @DataProvider
    public Object[][] validItems() {
        return new Object[][] {
            {new ItemBuilder().build()},
            {new ItemBuilder().without("description").build()},
            {new ItemBuilder().name("Pen").build()},
            {new ItemBuilder().name("a".repeat(100)).build()},
            {new ItemBuilder().price(0.01).build()},
            {new ItemBuilder().stock(0).build()},
            {new ItemBuilder().description("Small, light and cheap!").build()},
        };
    }

    @Test(dataProvider = "validItems")
    public void createValidItem(Map<String, Object> item) {
        APIResponse response = adminItems.create(item);

        assertEquals(response.status(), 201);
        JsonNode created = json(response);
        assertTrue(created.has("id"));
        assertEquals(created.get("name").asText(), item.get("name"));
        assertEquals(created.get("price").asDouble(), ((Number) item.get("price")).doubleValue());
        assertEquals(created.get("stock").asInt(), item.get("stock"));

        APIResponse saved = adminItems.get(created.get("id").asText());
        assertEquals(saved.status(), 200);
        assertEquals(json(saved), created);
    }

    @DataProvider
    public Object[][] invalidItems() {
        return new Object[][] {
            {new ItemBuilder().without("name").build(), "Item name is required."},
            {new ItemBuilder().name("").build(), "Item name cannot be empty."},
            {new ItemBuilder().name("ab").build(), "Item name should have a minimum length of 3."},
            {new ItemBuilder().name("a".repeat(101)).build(), "Item name should have a maximum length of 100."},
            {new ItemBuilder().name("Lamp#1").build(), "Item name must contain only alphanumeric characters and spaces."},
            {new ItemBuilder().description("Bad").build(), "Description should have a minimum length of 5."},
            {new ItemBuilder().description("Price <b>50</b>").build(), "Description contains invalid characters."},
            {new ItemBuilder().without("price").build(), "Price is required."},
            {new ItemBuilder().price("ten").build(), "Price must be a number."},
            {new ItemBuilder().price(0).build(), "Price must be a positive number."},
            {new ItemBuilder().price(-5).build(), "Price must be a positive number."},
            {new ItemBuilder().without("stock").build(), "Stock is required."},
            {new ItemBuilder().stock(-1).build(), "Stock cannot be negative."},
            {new ItemBuilder().stock(2.5).build(), "Stock must be an integer."},
        };
    }

    @Test(dataProvider = "invalidItems")
    public void createInvalidItem(Map<String, Object> item, String expectedError) {
        APIResponse response = adminItems.create(item);

        assertEquals(response.status(), 400);
        assertEquals(json(response).get("error").asText(), expectedError);
    }

    @Test
    public void adminCanCreateItemWithBasicAuth() {
        ItemClient items = new ItemClient(context(new BasicAuth(config.get("admin.username"), config.get("admin.password"))));

        assertEquals(items.create(new ItemBuilder().build()).status(), 201);
    }

    @Test
    public void customerCannotCreateItem() {
        ItemClient items = new ItemClient(loginAs(config.get("customer.username"), config.get("customer.password")));
        APIResponse response = items.create(new ItemBuilder().build());

        assertEquals(response.status(), 403);
        assertEquals(json(response).get("error").asText(), "Forbidden: customer does not have access.");
    }

    @Test
    public void cannotCreateItemWithoutLogin() {
        ItemClient items = new ItemClient(context(new NoAuth()));

        assertEquals(items.create(new ItemBuilder().build()).status(), 401);
    }
}
