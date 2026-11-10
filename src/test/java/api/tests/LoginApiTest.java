package api.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import api.auth.NoAuth;
import api.base.BaseApiTest;
import api.clients.AuthClient;
import api.clients.ItemClient;
import com.microsoft.playwright.APIResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginApiTest extends BaseApiTest {

    private AuthClient auth;

    @BeforeClass
    public void createClient() {
        auth = new AuthClient(context(new NoAuth()));
    }

    @DataProvider
    public Object[][] loginData() {
        return new Object[][] {
            {"admin", "adminpassword", 200, ""},
            {"user1", "userpassword", 200, ""},
            {"admin", "wrongpassword", 401, "Invalid credentials"},
            {"unknown", "userpassword", 401, "Invalid credentials"},
            {"ADMIN", "adminpassword", 401, "Invalid credentials"},
            {null, "userpassword", 400, "Username is required."},
            {"user1", null, 400, "Password is required."},
            {null, null, 400, "Username is required.; Password is required."},
            {"", "userpassword", 400, "Username cannot be empty."},
            {"user1", "", 400, "Password cannot be empty."},
            {"ab", "userpassword", 400, "Username should have a minimum length of 3."},
            {"a".repeat(31), "userpassword", 400, "Username should have a maximum length of 30."},
            {"user_1", "userpassword", 400, "Username must contain only alphanumeric characters."},
            {"user1", "12345", 400, "Password should have a minimum length of 6."},
        };
    }

    @Test(dataProvider = "loginData")
    public void login(String username, String password, int expectedStatus, String expectedError) {
        APIResponse response = auth.login(username, password);

        assertEquals(response.status(), expectedStatus);
        if (expectedStatus == 200) {
            assertFalse(json(response).get("token").asText().isEmpty());
        } else {
            assertEquals(json(response).get("error").asText(), expectedError);
        }
    }

    @Test
    public void tokenFromLoginOpensProtectedEndpoint() {
        APIResponse response = new ItemClient(loginAs("user1", "userpassword")).getAll();

        assertEquals(response.status(), 200);
        assertTrue(json(response).isArray());
    }
}
