package ui.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.testng.annotations.Test;
import ui.base.BaseTest;
import ui.pages.AddUserPage;
import ui.pages.AdminPage;
import ui.pages.DashboardPage;
import ui.pages.LoginPage;

public class AdminUserTest extends BaseTest {

    @Test
    public void addNewUserAndSearchForIt() {
        String username = "user" + System.currentTimeMillis();

        DashboardPage dashboard = new LoginPage(page).login(config.get("ui.username"), config.get("ui.password"));
        assertThat(dashboard.header()).hasText("Dashboard");

        AdminPage admin = dashboard.openAdminTab();
        AddUserPage addUser = admin.clickAdd();
        addUser.addUser("ESS", "a", "Enabled", username, "Test@12345");
        assertThat(addUser.toast()).containsText("Successfully Saved");

        admin.searchByUsername(username);
        assertThat(admin.resultRows()).hasCount(1);
        assertThat(admin.resultRows().first()).containsText(username);
    }
}
