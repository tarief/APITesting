package ui.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import config.ConfigReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected ConfigReader config = ConfigReader.getInstance();

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        boolean headless = Boolean.parseBoolean(config.get("headless"));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        page = browser.newPage();
        page.navigate(config.get("ui.url"));
    }

    @AfterMethod
    public void tearDown() {
        browser.close();
        playwright.close();
    }
}
