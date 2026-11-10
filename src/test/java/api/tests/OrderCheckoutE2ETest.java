package api.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import api.base.BaseApiTest;
import api.builders.ItemBuilder;
import api.builders.OrderBuilder;
import api.clients.ItemClient;
import api.clients.OrderClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

// Flow from docs/API_FLOWS.md: "Creating and Checking Out an Order"
public class OrderCheckoutE2ETest extends BaseApiTest {

    private ItemClient adminItems;
    private ItemClient customerItems;
    private OrderClient adminOrders;
    private OrderClient customerOrders;
    private String customerId;
    private String itemId;
    private String orderId;

    @BeforeClass
    public void login() {
        APIRequestContext admin = loginAs(config.get("admin.username"), config.get("admin.password"));
        APIRequestContext customer = loginAs(config.get("customer.username"), config.get("customer.password"));
        adminItems = new ItemClient(admin);
        adminOrders = new OrderClient(admin);
        customerItems = new ItemClient(customer);
        customerOrders = new OrderClient(customer);
        customerId = config.get("customer.username");
    }

    @Test
    public void adminCreatesItem() {
        APIResponse response = adminItems.create(new ItemBuilder().name("Office Chair").price(40).stock(5).build());

        assertEquals(response.status(), 201);
        itemId = json(response).get("id").asText();
    }

    @Test(dependsOnMethods = "adminCreatesItem")
    public void customerSeesItemInList() {
        APIResponse response = customerItems.getAll();

        assertEquals(response.status(), 200);
        assertTrue(contains(json(response), itemId), "item not in the list");
    }

    @Test(dependsOnMethods = "customerSeesItemInList")
    public void adminCreatesOrderForCustomer() {
        APIResponse response = adminOrders.create(new OrderBuilder().customerId(customerId).item(itemId, 2).build());

        assertEquals(response.status(), 201);
        JsonNode order = json(response);
        orderId = order.get("id").asText();
        assertEquals(order.get("customerId").asText(), customerId);
        assertEquals(order.get("status").asText(), "pending");
        assertEquals(order.get("paid").asBoolean(), false);
        assertEquals(order.get("total").asDouble(), 80.0);

        assertEquals(json(adminItems.get(itemId)).get("stock").asInt(), 3, "stock was not reduced");
    }

    @Test(dependsOnMethods = "adminCreatesOrderForCustomer")
    public void customerChecksOutOrder() {
        APIResponse response = customerOrders.checkout(orderId);

        assertEquals(response.status(), 200);
        assertEquals(json(response).get("status").asText(), "paid");
        assertEquals(json(response).get("paid").asBoolean(), true);
    }

    @Test(dependsOnMethods = "customerChecksOutOrder")
    public void customerSeesOrderInOwnOrders() {
        APIResponse response = customerOrders.getAll();

        assertEquals(response.status(), 200);
        assertTrue(contains(json(response), orderId), "order not in customer orders");
        for (JsonNode order : json(response)) {
            assertEquals(order.get("customerId").asText(), customerId);
        }
    }

    @Test(dependsOnMethods = "customerChecksOutOrder")
    public void adminSeesOrderInPaidOrders() {
        APIResponse response = adminOrders.getPaid();

        assertEquals(response.status(), 200);
        assertTrue(contains(json(response), orderId), "order not in paid orders");
    }

    private boolean contains(JsonNode list, String id) {
        for (JsonNode node : list) {
            if (node.get("id").asText().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
