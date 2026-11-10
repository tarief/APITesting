package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AddUserPage {

    private final Page page;

    public AddUserPage(Page page) {
        this.page = page;
    }

    private Locator field(String label) {
        return page.locator(".oxd-input-group:has(label:text-is('" + label + "'))");
    }

    private void select(String label, String option) {
        field(label).locator(".oxd-select-text").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(option)).click();
    }

    public void addUser(String role, String employeeHint, String status, String username, String password) {
        select("User Role", role);
        field("Employee Name").locator("input").fill(employeeHint);
        page.locator(".oxd-autocomplete-option:not(:has-text('Searching'))").first().click();
        select("Status", status);
        field("Username").locator("input").fill(username);
        field("Password").locator("input").fill(password);
        field("Confirm Password").locator("input").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
    }

    public Locator toast() {
        return page.locator(".oxd-toast");
    }
}
