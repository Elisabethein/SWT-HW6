import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This work is done by:
 * C14544 Ralf Animägi ralfanimagi@gmail.com
 * B911205 Elisabet Hein elisabet.hein@ut.ee
 */
public class TestHelper {

    static WebDriver driver;
    final int waitForResposeTime = 4;
    String baseUrlAdmin = "http://127.0.0.1:3000/admin";
    String baseUrlUsers = "http://127.0.0.1:3000/users";
    String baseUrlProducts = "http://127.0.0.1:3000/products";
    String baseUrl = "http://127.0.0.1:3000/";

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\elisabeh\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(baseUrl);
    }

    // Method to go to a page
    void goToPage(String page) {
        WebElement elem = driver.findElement(By.linkText(page));
        elem.click();
        waitForElementById(page);
    }

    void waitForElementById(String id) {
        new WebDriverWait(driver, waitForResposeTime).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    // Method to verify if an element is present
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Method to login
    void login(String username, String password) {
        driver.get(baseUrlAdmin);
        goToPage("Login");
        driver.findElement(By.id("name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        By loginButtonXpath = By.xpath("//input[@value='Login']");
        driver.findElement(loginButtonXpath).click();
    }

    // Method to register
    void register(String username, String password) {
        driver.get(baseUrlAdmin);
        goToPage("Register");
        driver.findElement(By.id("user_name")).sendKeys(username);
        driver.findElement(By.id("user_password")).sendKeys(password);
        driver.findElement(By.id("user_password_confirmation")).sendKeys(password);
        driver.findElement(By.name("commit")).click();
    }

    // Method to logout
    void logout() {
        WebElement logout = driver.findElement(By.linkText("Logout"));
        logout.click();
        waitForElementById("Admin");
    }

    // Method to delete an account
    void deleteAccount(String username) {
        driver.get(baseUrlUsers);
        WebElement deleteBody = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = deleteBody.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            String name = row.findElement(By.className("name")).getText();
            if (name.equals(username)) {
                row.findElement(By.xpath(".//a[contains(text(), 'Destroy')]")).click();
                Alert alert = driver.switchTo().alert();
                alert.accept();
                break;
            }
        }
    }

    // Method to add products
    void addProductshelp(String title, String description, String type, String price) {
        driver.get(baseUrlProducts);
        driver.findElement(By.linkText("New product")).click();
        driver.findElement(By.id("product_title")).sendKeys(title);
        driver.findElement(By.id("product_description")).sendKeys(description);
        WebElement dropDown = driver.findElement(By.id("product_prod_type"));
        Select select = new Select(dropDown);
        select.selectByVisibleText(type);
        driver.findElement(By.id("product_price")).sendKeys(price);
        driver.findElement(By.name("commit")).click();
    }

    // Method to edit products
    void editProducthelp(String title, String newTitle, String newDescription, String newType, String newPrice) {
        driver.get(baseUrlProducts);
        WebElement row = driver.findElement(By.xpath("//tr[td/a[text()='" + title + "']]"));
        row.findElement(By.xpath(".//a[contains(text(), 'Edit')]")).click();
        driver.findElement(By.id("product_title")).clear();
        driver.findElement(By.id("product_title")).sendKeys(newTitle);
        driver.findElement(By.id("product_description")).clear();
        driver.findElement(By.id("product_description")).sendKeys(newDescription);
        WebElement dropDown = driver.findElement(By.id("product_prod_type"));
        Select select = new Select(dropDown);
        select.selectByVisibleText(newType);
        driver.findElement(By.id("product_price")).clear();
        driver.findElement(By.id("product_price")).sendKeys(newPrice);
        driver.findElement(By.name("commit")).click();
    }

    // Method to delete products
    void deleteProduct(String title) {
        driver.get(baseUrlProducts);
        WebElement row = driver.findElement(By.xpath("//tr[td/a[text()='" + title + "']]"));
        row.findElement(By.xpath(".//a[contains(text(), 'Delete')]")).click();
    }

    // Method to find a product
    boolean findProduct(String title) {
        driver.get(baseUrlProducts);
        return isElementPresent(By.linkText(title));
    }

    // Method to add to cart
    void addToCart(String title) {
        driver.get(baseUrl);
        WebElement div = driver.findElement(By.id(title + "_entry"));
        WebElement form = div.findElement(By.className("button_to"));
        form.submit();
    }

    // Method to get quantity
    String getQuantity(String title) {
        driver.get(baseUrl);
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody .cart_row"));
        for (WebElement row : rows) {
            String itemTitle = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            if (itemTitle.equals(title)) {
                return row.findElement(By.cssSelector("td:nth-child(1)")).getText();
            }
        }
        return null;
    }

    // Method to decrease quantity
    void decreaseQuantity(String title) {
        driver.get(baseUrl);
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody .cart_row"));
        for (WebElement row : rows) {
            String itemTitle = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            if (itemTitle.equals(title)) {
                WebElement decreaseButton = row.findElement(By.cssSelector(".quantity a[data-method='put'][href*='/decrease']"));
                decreaseButton.click();
                break;
            }
        }
    }

    // Method to increase quantity
    void increaseQuantity(String title) {
        driver.get(baseUrl);
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody .cart_row"));
        for (WebElement row : rows) {
            String itemTitle = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            if (itemTitle.equals(title)) {
                WebElement increaseButton = row.findElement(By.cssSelector(".quantity a[data-method='put'][href*='/increase']"));
                increaseButton.click();
                break;
            }
        }
    }

    // Method to delete item
    void deleteItem(String title) {
        driver.get(baseUrl);
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody .cart_row"));
        for (WebElement row : rows) {
            String itemTitle = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            if (itemTitle.equals(title)) {
                WebElement deleteButton = row.findElement(By.cssSelector("td:nth-child(6)"));
                deleteButton.click();
                break;
            }
        }
    }

    // Method to get cart length
    int getCartLength() {
        driver.get(baseUrl);
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody .cart_row"));
        return rows.size();
    }

    // Method to empty cart
    void emptyCart() {
        driver.get(baseUrl);
        WebElement emptyCartForm = driver.findElement(By.cssSelector("form.button_to"));
        emptyCartForm.submit();
    }

    // Method to checkout with name, address, email, and pay type
    void checkout(String name, String address, String email, String payType) {
        driver.get(baseUrl);
        WebElement checkoutButton = driver.findElement(By.cssSelector("form#checkout_button"));
        checkoutButton.submit();
        driver.findElement(By.id("order_name")).sendKeys(name);
        driver.findElement(By.id("order_address")).sendKeys(address);
        driver.findElement(By.id("order_email")).sendKeys(email);
        WebElement dropDown = driver.findElement(By.id("order_pay_type"));
        Select select = new Select(dropDown);
        select.selectByVisibleText(payType);
        driver.findElement(By.name("commit")).click();
    }

    // Method to search
    void search(String query) {
        driver.get(baseUrl);
        WebElement searchInput = driver.findElement(By.id("search_input"));
        searchInput.clear();
        searchInput.sendKeys(query);
    }

    // Method to iterate over items and check if they contain the query
    boolean iterOverItems(String query) {
        List<WebElement> items = driver.findElements(By.cssSelector("div.entry"));
        for (WebElement item : items) {
            String s = item.findElement(By.cssSelector("h3 a")).getText().toLowerCase();
            // we need to exclude hidden entries
            if (!s.contains(query.toLowerCase()) && !s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Method to check categories of items
    boolean checkCategories(String category) {
        List<WebElement> items = driver.findElements(By.cssSelector("div.entry"));
        for (WebElement item : items) {
            String categoryText = item.findElement(By.cssSelector("#category")).getText().split(":")[1].trim();
            if (!categoryText.equals(category)) return false;
        }
        return true;
    }


    @After
    public void tearDown() {
        driver.close();
    }

}