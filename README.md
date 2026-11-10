# UI and API Test Framework

Java 17, Maven, Playwright for Java and TestNG.

- **UI**: https://opensource-demo.orangehrmlive.com/ — log in as Admin, open the Admin tab, add a new user, then search for it.
- **API**: the capstone project (Node/Express API on `http://localhost:3000`).
  - E2E flow from `docs/API_FLOWS.md` "Creating and Checking Out an Order": admin creates an item, customer sees it, admin creates an order for the customer, customer checks out, the order shows in the customer's orders and in the admin's paid orders.
  - Full testing with different data for 2 APIs: `POST /login` and `POST /items`.

## Design patterns

| Pattern | Where |
| :-- | :-- |
| Page Object Model (UI) | `ui/pages` |
| Singleton | `config/ConfigReader` |
| Factory (API) | `api/core/RequestContextFactory` builds every request context |
| Strategy (API) | `api/auth` — `BearerTokenAuth`, `BasicAuth`, `NoAuth` |
| Builder (API) | `api/builders` — `ItemBuilder`, `OrderBuilder` |

## Project structure

```
src/test/java
  config/        ConfigReader
  ui/base        BaseTest
  ui/pages       LoginPage, DashboardPage, AdminPage, AddUserPage
  ui/tests       AdminUserTest
  api/auth       AuthStrategy + BearerTokenAuth, BasicAuth, NoAuth
  api/core       RequestContextFactory
  api/builders   ItemBuilder, OrderBuilder
  api/clients    AuthClient, ItemClient, OrderClient
  api/base       BaseApiTest
  api/tests      OrderCheckoutE2ETest, LoginApiTest, CreateItemApiTest
src/test/resources/config.properties
testng.xml
```

## How to run

1. Start the capstone API first (from the capstone project folder):
   ```
   npm install
   npm run build
   npm start
   ```
2. Run the tests from this folder:
   ```
   mvn test
   ```
   Only UI: `mvn test -Dtest=AdminUserTest`
   Only API: `mvn test -Dtest=OrderCheckoutE2ETest,LoginApiTest,CreateItemApiTest`
   Headless: `mvn test -Dheadless=true`

Report: `target/surefire-reports/index.html`
