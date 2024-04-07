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

public class TestHelper {

    static WebDriver driver;
    final int waitForResposeTime = 4;

    // here write a link to your admin website (e.g. http://my-app.herokuapp.com/admin)
    String baseUrlAdmin = "http://127.0.0.1:3000/admin";
    String baseUrlUsers = "http://127.0.0.1:3000/users";
    String baseUrlProducts = "http://127.0.0.1:3000/products";

    String orderUrl = "http://127.0.0.1:3000/orderd/new?";
    String baseUrl = "http://127.0.0.1:3000/";

    @Before
    public void setUp() {

        // if you use Chrome:
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\elisabeh\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();

        // if you use Firefox:
        //System.setProperty("webdriver.gecko.driver", "C:\\Users\\...\\geckodriver.exe");
        //driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(baseUrl);

    }

    void goToPage(String page) {
        WebElement elem = driver.findElement(By.linkText(page));
        elem.click();
        waitForElementById(page);
    }

    void waitForElementById(String id) {
        new WebDriverWait(driver, waitForResposeTime).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    void login(String username, String password) {

        driver.get(baseUrlAdmin);

        goToPage("Login");

        driver.findElement(By.id("name")).sendKeys(username);

        driver.findElement(By.id("password")).sendKeys(password);

        By loginButtonXpath = By.xpath("//input[@value='Login']");

        driver.findElement(loginButtonXpath).click();
    }

    void register(String username, String password) {
        driver.get(baseUrlAdmin);

        goToPage("Register");

        driver.findElement(By.id("user_name")).sendKeys(username);

        driver.findElement(By.id("user_password")).sendKeys(password);

        driver.findElement(By.id("user_password_confirmation")).sendKeys(password);

        driver.findElement(By.name("commit")).click();
    }

    void logout() {
        WebElement logout = driver.findElement(By.linkText("Logout"));
        logout.click();
        waitForElementById("Admin");
    }

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

    void deleteProduct(String title) {
        driver.get(baseUrlProducts);
        WebElement row = driver.findElement(By.xpath("//tr[td/a[text()='" + title + "']]"));
        row.findElement(By.xpath(".//a[contains(text(), 'Delete')]")).click();
    }

    boolean findProduct(String title) {
        driver.get(baseUrlProducts);
        return isElementPresent(By.linkText(title));
    }

    void addToCart(String title) {
        driver.get(baseUrl);
        WebElement div = driver.findElement(By.id(title + "_entry"));
        WebElement form = div.findElement(By.className("button_to"));
        form.submit();
    }

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

    int getCartLength() {
        driver.get(baseUrl);
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody .cart_row"));
        return rows.size();
    }

    void emptyCart() {
        driver.get(baseUrl);
        WebElement emptyCartForm = driver.findElement(By.cssSelector("form.button_to"));
        emptyCartForm.submit();
    }

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

    void search(String query) {
        driver.get(baseUrl);
        WebElement searchInput = driver.findElement(By.id("search_input"));
        searchInput.clear();
        searchInput.sendKeys(query);
    }

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