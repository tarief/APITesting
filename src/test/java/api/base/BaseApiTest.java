package api.base;

import api.auth.AuthStrategy;
import api.auth.BearerTokenAuth;
import api.auth.NoAuth;
import api.clients.AuthClient;
import api.core.RequestContextFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import config.ConfigReader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    protected ConfigReader config = ConfigReader.getInstance();
    protected Playwright playwright;
    protected RequestContextFactory factory;

    @BeforeClass
    public void setUp() {
        playwright = Playwright.create();
        factory = new RequestContextFactory(playwright);
    }

    @AfterClass
    public void tearDown() {
        playwright.close();
    }

    protected APIRequestContext context(AuthStrategy auth) {
        return factory.create(auth);
    }

    protected APIRequestContext loginAs(String username, String password) {
        APIResponse response = new AuthClient(context(new NoAuth())).login(username, password);
        String token = json(response).get("token").asText();
        return context(new BearerTokenAuth(token));
    }

    protected JsonNode json(APIResponse response) {
        try {
            return new ObjectMapper().readTree(response.text());
        } catch (Exception e) {
            throw new RuntimeException("Response is not JSON: " + response.text(), e);
        }
    }
}
