package api.core;

import api.auth.AuthStrategy;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import config.ConfigReader;

// Factory: the only place that knows how to build a request context (base url + auth headers)
public class RequestContextFactory {

    private final Playwright playwright;

    public RequestContextFactory(Playwright playwright) {
        this.playwright = playwright;
    }

    public APIRequestContext create(AuthStrategy auth) {
        return playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(ConfigReader.getInstance().get("api.url"))
                .setExtraHTTPHeaders(auth.headers()));
    }
}
