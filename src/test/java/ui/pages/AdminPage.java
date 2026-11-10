package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AdminPage {

    private final Page page;

    public AdminPage(Page page) {
        this.page = page;
    }

    public AddUserPage clickAdd() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
        return new AddUserPage(page);
    }

    public void searchByUsername(String username) {
        page.waitForURL("**/admin/viewSystemUsers");
        page.locator(".oxd-input-group:has(label:text-is('Username')) input").fill(username);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
    }

    public Locator resultRows() {
        return page.locator(".oxd-table-card");
    }
}
