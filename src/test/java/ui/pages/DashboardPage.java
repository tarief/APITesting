package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class DashboardPage {

    private final Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

    public Locator header() {
        return page.locator(".oxd-topbar-header-breadcrumb");
    }

    public AdminPage openAdminTab() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Admin")).click();
        return new AdminPage(page);
    }
}
