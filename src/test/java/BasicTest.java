import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.*;

public class BasicTest extends TestHelper {


    private String username = "123";
    private String password = "123";

    @Test
    public void titleExistsTest() {
        String expectedTitle = "ST Online Store";
        String actualTitle = driver.getTitle();
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    public void loginLogoutTest() {
        login(username, password);
        WebElement adminMenu = driver.findElement(By.id("menu"));
        WebElement firstMenuItem = adminMenu.findElement(By.cssSelector("li:first-child"));
        assertEquals("Admin", firstMenuItem.getText());
        logout();
    }


    @Test
    public void loginFalsePassword() {
        login(username, "1234");
        WebElement error = driver.findElement(By.id("notice"));
        assertEquals("Invalid user/password combination", error.getText());
    }

    @Test
    public void registerAndDeleteAnAccount() {
        register("test", "test");
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("User test was successfully created.", notice.getText());
        deleteAccount("test");
    }

    @Test
    public void addProductsBooks() {
        login(username, password);
        addProductshelp("Test Product", "Test Description", "Books", "10");
        assertTrue(findProduct("Test Product"));
        deleteProduct("Test Product");
    }

    @Test
    public void addProductsSunglasses() {
        login(username, password);
        addProductshelp("Test Product 2", "Test Description", "Sunglasses", "10");
        assertTrue(findProduct("Test Product 2"));
        deleteProduct("Test Product 2");
    }

    @Test
    public void addProductsOther() {
        login(username, password);
        addProductshelp("Test Product 3", "Test Description", "Other", "10");
        assertTrue(findProduct("Test Product 3"));
        deleteProduct("Test Product 3");
    }

    @Test
    public void addProductsNegativePrice() {
        login(username, password);
        addProductshelp("Test Product 4", "Test Description", "Books", "-10");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
    }

    @Test
    public void addAlreadyExistingProduct() {
        login(username, password);
        addProductshelp("Test Product Existing", "Test Description", "Other", "10");
        addProductshelp("Test Product Existing", "Test Description", "Other", "10");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
        deleteProduct("Test Product Existing");
    }

    @Test
    public void addProductsWithEmptyFields() {
        login(username, password);
        addProductshelp("", "123", "Other", "123");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
    }

    @Test
    public void editProduct() {
        login(username, password);
        addProductshelp("Test Product 5", "Test Description", "Books", "10");
        editProducthelp("Test Product 5", "Test Product 6", "Test Description 2", "Books", "10");
        assertTrue(findProduct("Test Product 6"));
        deleteProduct("Test Product 6");
    }

    @Test
    public void deleteProduct() {
        login(username, password);
        addProductshelp("Test Product 7", "Test Description", "Books", "10");
        deleteProduct("Test Product 7");
        assertFalse(findProduct("Test Product 7"));
    }

    // FAIL 1 - too large price
    @Test
    public void addProductLargePrice() {
        login(username, password);
        addProductshelp("Test Product 8", "Test Description", "Books", "10000000");
        assertTrue(findProduct("Test Product 8"));
    }

    // End User tests
    @Test
    public void addProductsToCart() {
        addToCart("B45593 Sunglasses");
        assertTrue(isElementPresent(By.id("checkout_button")));
    }

    @Test
    public void increaseProductQuantity() {
        addToCart("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        assertEquals(getQuantity("B45593 Sunglasses"), "2×");
    }

    @Test
    public void decreaseProductQuantity() {
        addToCart("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        decreaseQuantity("B45593 Sunglasses");
        assertEquals(getQuantity("B45593 Sunglasses"), "2×");
    }

    @Test
    public void deleteProductFromCart() {
        addToCart("B45593 Sunglasses");
        deleteItem("B45593 Sunglasses");
        assertFalse(isElementPresent(By.id("checkout_button")));
    }

    @Test
    public void deleteMultipleProductFromCart() {
        addToCart("B45593 Sunglasses");
        addToCart("Sunglasses 2AR");
        deleteItem("B45593 Sunglasses");
        assertEquals(getCartLength(), 1);
    }

    @Test
    public void deleteCart() {
        addToCart("B45593 Sunglasses");
        addToCart("Sunglasses 2AR");
        emptyCart();
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("Cart successfully deleted.", notice.getText());
    }

    @Test
    public void purchaseItems() {
        addToCart("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        checkout("name", "address", "email.com", "Credit card");
        assertEquals(driver.findElement(By.className("total_cell")).getText(), "€52.00");
    }

    @Test
    public void purchaseItemsWithMissingFields() {
        addToCart("B45593 Sunglasses");
        checkout("", "", "email.com", "Credit card");
        assertTrue(isElementPresent(By.id("error_explanation")));
    }

    @Test
    public void search() {
        search("sun");
        assertTrue(iterOverItems("sun"));
    }

    @Test
    public void selectBooks() {
        goToPage("Books");
        assertTrue(checkCategories("Books"));
    }

    @Test
    public void selectSunglasses() {
        goToPage("Sunglasses");
        assertTrue(checkCategories("Sunglasses"));
    }

    @Test
    public void selectOther() {
        goToPage("Other");
        assertTrue(checkCategories("Other"));
    }


}
